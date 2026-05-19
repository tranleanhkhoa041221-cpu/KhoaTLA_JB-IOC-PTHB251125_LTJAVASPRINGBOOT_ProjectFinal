package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.edu.config.principal.UserPrincipal;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.InternshipAssignmentCreateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateStatusRequest;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.*;
import ra.edu.mapper.InternshipAssignmentMapper;
import ra.edu.repository.InternshipAssignmentRepository;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.service.InternshipAssignmentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements InternshipAssignmentService {

    private final InternshipAssignmentRepository internshipAssignmentRepository;

    private final StudentRepository studentRepository;

    private final MentorRepository mentorRepository;

    private final InternshipPhaseRepository internshipPhaseRepository;

    private final InternshipAssignmentMapper internshipAssignmentMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<InternshipAssignmentResponse> getAllAssignments(
            int page,
            int size,
            Long studentId,
            Long mentorId,
            Long phaseId,
            String studentUsername,
            String mentorUsername,
            String studentFullName,
            String mentorFullName,
            String studentEmail,
            String mentorEmail,
            String studentPhoneNumber,
            String mentorPhoneNumber,
            InternshipAssignmentsStatus status,
            LocalDateTime assignedDate,
            LocalDateTime minAssignedDate,
            LocalDateTime maxAssignedDate) {

        if (minAssignedDate != null && maxAssignedDate != null
                && minAssignedDate.isAfter(maxAssignedDate)) {

            throw new IllegalArgumentException(
                    "minAssignedDate không được sau maxAssignedDate");
        }

        if (studentId != null && !studentRepository.existsById(studentId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy Student với ID = " + studentId);
        }

        if (mentorId != null && !mentorRepository.existsById(mentorId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy Mentor với ID = " + mentorId);
        }

        if (phaseId != null && !internshipPhaseRepository.existsById(phaseId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy InternshipPhase với ID = " + phaseId);
        }

        User currentUser = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("assignmentId").descending());

        Page<InternshipAssignment> assignmentPage;

        if (currentUser.getRole() == UserRole.ADMIN) {

            if (studentId != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentId(studentId, pageable);

            } else if (mentorId != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorId(mentorId, pageable);

            } else if (phaseId != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByPhase_PhaseId(phaseId, pageable);

            } else if (status != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStatus(status, pageable);

            } else if (assignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByAssignedDate(assignedDate, pageable);

            } else if (minAssignedDate != null && maxAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByAssignedDateBetween(
                                        minAssignedDate, maxAssignedDate, pageable);

            } else if (minAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByAssignedDateGreaterThanEqual(
                                        minAssignedDate, pageable);

            } else if (maxAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByAssignedDateLessThanEqual(
                                        maxAssignedDate, pageable);

            } else if (studentUsername != null && !studentUsername.isBlank()) {

                assignmentPage = internshipAssignmentRepository
                        .findAllByStudent_User_UsernameContainingIgnoreCase(
                                studentUsername, pageable);

            } else if (studentFullName != null && !studentFullName.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_User_FullNameContainingIgnoreCase(
                                        studentFullName, pageable);

            } else if (studentEmail != null && !studentEmail.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_User_EmailContainingIgnoreCase(
                                        studentEmail, pageable);

            } else if (studentPhoneNumber != null && !studentPhoneNumber.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_User_PhoneNumberContainingIgnoreCase(
                                        studentPhoneNumber, pageable);

            } else if (mentorUsername != null && !mentorUsername.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_User_UsernameContainingIgnoreCase(
                                        mentorUsername, pageable);

            } else if (mentorFullName != null && !mentorFullName.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_User_FullNameContainingIgnoreCase(
                                        mentorFullName, pageable);

            } else if (mentorEmail != null && !mentorEmail.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_User_EmailContainingIgnoreCase(
                                        mentorEmail, pageable);

            } else if (mentorPhoneNumber != null && !mentorPhoneNumber.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_User_PhoneNumberContainingIgnoreCase(
                                        mentorPhoneNumber, pageable);

            } else {

                assignmentPage =
                        internshipAssignmentRepository.findAll(pageable);
            }
        } else if (currentUser.getRole() == UserRole.MENTOR) {

            Mentor mentor =
                    mentorRepository.findByUser_UserId(currentUser.getUserId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role MENTOR"));

            if (studentId != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndStudent_StudentId(
                                        mentor.getMentorId(), studentId, pageable);

            } else if (phaseId != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndPhase_PhaseId(
                                        mentor.getMentorId(), phaseId, pageable);

            } else if (status != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndStatus(
                                        mentor.getMentorId(), status, pageable);

            } else if (assignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndAssignedDate(
                                        mentor.getMentorId(),
                                        assignedDate, pageable);

            } else if (minAssignedDate != null && maxAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndAssignedDateBetween(
                                        mentor.getMentorId(),
                                        minAssignedDate, maxAssignedDate, pageable);

            } else if (minAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndAssignedDateGreaterThanEqual(
                                        mentor.getMentorId(),
                                        minAssignedDate, pageable);

            } else if (maxAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndAssignedDateLessThanEqual(
                                        mentor.getMentorId(),
                                        maxAssignedDate, pageable);

            } else if (studentUsername != null && !studentUsername.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository.
                                findAllByMentor_MentorIdAndStudent_User_UsernameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentUsername, pageable);

            } else if (studentFullName != null && !studentFullName.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndStudent_User_FullNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentFullName, pageable);

            } else if (studentEmail != null && !studentEmail.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndStudent_User_EmailContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentEmail, pageable);

            } else if (studentPhoneNumber != null && !studentPhoneNumber.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndStudent_User_PhoneNumberContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentPhoneNumber, pageable);

            } else if (mentorUsername != null && !mentorUsername.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndMentor_User_UsernameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorUsername, pageable);

            } else if (mentorFullName != null && !mentorFullName.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndMentor_User_FullNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorFullName, pageable);

            } else if (mentorEmail != null && !mentorEmail.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndMentor_User_EmailContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorEmail, pageable);

            } else if (mentorPhoneNumber != null && !mentorPhoneNumber.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorIdAndMentor_User_PhoneNumberContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorPhoneNumber, pageable);

            } else {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByMentor_MentorId(
                                        mentor.getMentorId(), pageable);
            }
        } else {

            Student student =
                    studentRepository.findByUser_UserId(currentUser.getUserId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role STUDENT"));

            if (mentorId != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndMentor_MentorId(
                                        student.getStudentId(),
                                        mentorId, pageable);

            } else if (phaseId != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndPhase_PhaseId(
                                        student.getStudentId(),
                                        phaseId, pageable);

            } else if (status != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndStatus(
                                        student.getStudentId(),
                                        status, pageable);

            } else if (assignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndAssignedDate(
                                        student.getStudentId(),
                                        assignedDate, pageable);

            } else if (minAssignedDate != null && maxAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndAssignedDateBetween(
                                        student.getStudentId(),
                                        minAssignedDate, maxAssignedDate, pageable);

            } else if (minAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndAssignedDateGreaterThanEqual(
                                        student.getStudentId(),
                                        minAssignedDate, pageable);

            } else if (maxAssignedDate != null) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndAssignedDateLessThanEqual(
                                        student.getStudentId(),
                                        maxAssignedDate, pageable);

            } else if (mentorUsername != null && !mentorUsername.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndMentor_User_UsernameContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorUsername, pageable);

            } else if (mentorFullName != null && !mentorFullName.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndMentor_User_FullNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorFullName, pageable);

            } else if (mentorEmail != null && !mentorEmail.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndMentor_User_EmailContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorEmail, pageable);

            } else if (mentorPhoneNumber != null && !mentorPhoneNumber.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndMentor_User_PhoneNumberContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorPhoneNumber, pageable);

            } else if (studentUsername != null && !studentUsername.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndStudent_User_UsernameContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentUsername, pageable);

            } else if (studentFullName != null && !studentFullName.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndStudent_User_FullNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentFullName, pageable);

            } else if (studentEmail != null && !studentEmail.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndStudent_User_EmailContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentEmail, pageable);

            } else if (studentPhoneNumber != null && !studentPhoneNumber.isBlank()) {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentIdAndStudent_User_PhoneNumberContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentPhoneNumber, pageable);

            } else {

                assignmentPage =
                        internshipAssignmentRepository
                                .findAllByStudent_StudentId(
                                        student.getStudentId(), pageable);
            }
        }

        List<InternshipAssignmentResponse> items =
                assignmentPage.getContent()
                        .stream()
                        .map(internshipAssignmentMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(assignmentPage.getTotalPages())
                .totalItems(assignmentPage.getTotalElements())
                .build();

        return PaginationResponse.<InternshipAssignmentResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public InternshipAssignmentResponse getAssignmentById(Long id) {

        InternshipAssignment assignment =
                internshipAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = " + id));

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.ADMIN) {

            return internshipAssignmentMapper.toResponse(assignment);
        }

        if (currentUser.getRole() == UserRole.MENTOR) {

            Mentor mentor =
                    mentorRepository.findByUser_UserId(
                                    currentUser.getUserId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role MENTOR"));

            if (!assignment.getMentor().getMentorId()
                    .equals(mentor.getMentorId())) {

                throw new AccessDeniedException(
                        "Mentor ID = "
                                + mentor.getMentorId()
                                + " không có quyền xem Assignment ID = "
                                + assignment.getAssignmentId());
            }

            return internshipAssignmentMapper.toResponse(assignment);
        }

        Student student =
                studentRepository.findByUser_UserId(
                                currentUser.getUserId()
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role STUDENT"));


        if (!assignment.getStudent().getStudentId()
                .equals(student.getStudentId())) {

            throw new AccessDeniedException(
                    "Student ID = "
                            + student.getStudentId()
                            + " không có quyền xem Assignment ID = "
                            + assignment.getAssignmentId());
        }

        return internshipAssignmentMapper.toResponse(assignment);
    }

    @Override
    public InternshipAssignmentResponse createAssignment(
            InternshipAssignmentCreateRequest request) {

        Student student =
                studentRepository.findById(request.getStudentId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Student với ID = "
                                                + request.getStudentId()));

        Mentor mentor =
                mentorRepository.findById(request.getMentorId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Mentor với ID = "
                                                + request.getMentorId()));

        InternshipPhase phase =
                internshipPhaseRepository.findById(request.getPhaseId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy InternshipPhase với ID = "
                                                + request.getPhaseId()));

        if (internshipAssignmentRepository
                .existsByStudent_StudentIdAndPhase_PhaseId(
                        request.getStudentId(),
                        request.getPhaseId())) {

            throw new IllegalStateException(
                    "Student ID = "
                            + request.getStudentId()
                            + " đã được phân công trong Phase ID = "
                            + request.getPhaseId());
        }

        InternshipAssignment assignment = internshipAssignmentMapper.toEntity(request);

        assignment.setStudent(student);

        assignment.setMentor(mentor);

        assignment.setPhase(phase);

        assignment.setStatus(InternshipAssignmentsStatus.PENDING);

        assignment.setCreatedAt(LocalDateTime.now());

        assignment.setUpdatedAt(LocalDateTime.now());

        internshipAssignmentRepository.save(assignment);

        return internshipAssignmentMapper.toResponse(assignment);
    }

    @Override
    public InternshipAssignmentResponse updateAssignment(
            Long id,
            InternshipAssignmentUpdateRequest request) {

        InternshipAssignment assignment =
                internshipAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = " + id));

        boolean used =
                assignment.getAssessmentResults() != null
                        && !assignment.getAssessmentResults().isEmpty();

        if (used
                && request.getMentorId() != null && !assignment.getMentor()
                .getMentorId().equals(request.getMentorId())) {

            throw new IllegalStateException(
                    "Không thể cập nhật Mentor cho Assignment ID = "
                            + id
                            + " vì assignment đã liên kết với AssessmentResult");
        }

        if (request.getMentorId() != null) {

            Mentor mentor =
                    mentorRepository.findById(request.getMentorId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Không tìm thấy Mentor với ID = "
                                                    + request.getMentorId()));

            assignment.setMentor(mentor);
        }

        internshipAssignmentMapper.updateEntityFromDto(request, assignment);

        assignment.setUpdatedAt(LocalDateTime.now());

        internshipAssignmentRepository.save(assignment);

        return internshipAssignmentMapper.toResponse(assignment);
    }

    @Override
    public InternshipAssignmentResponse updateAssignmentStatus(
            Long id,
            InternshipAssignmentUpdateStatusRequest request) {

        InternshipAssignment assignment =
                internshipAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = " + id));

//        assignment.setStatus(request.getStatus());
        internshipAssignmentMapper.updateStatusFromDto(request, assignment);

        assignment.setUpdatedAt(LocalDateTime.now());

        internshipAssignmentRepository.save(assignment);

        return internshipAssignmentMapper.toResponse(assignment);
    }

    @Override
    public InternshipAssignmentResponse deleteAssignment(Long id) {

        InternshipAssignment assignment =
                internshipAssignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = " + id));

        if (assignment.getAssessmentResults() != null
                && !assignment.getAssessmentResults().isEmpty()) {

            throw new IllegalStateException(
                    "Không thể xóa Assignment ID = "
                            + id
                            + " vì đã liên kết với AssessmentResult");
        }

        InternshipAssignmentResponse response =
                internshipAssignmentMapper.toResponse(assignment);

        internshipAssignmentRepository.delete(assignment);

        return response;
    }
}