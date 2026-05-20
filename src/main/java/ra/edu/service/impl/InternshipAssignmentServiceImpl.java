package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.edu.config.principal.UserPrincipal;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.InternshipAssignmentCreateRequest;
import ra.edu.dto.request.InternshipAssignmentFilterRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateStatusRequest;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.*;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
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

    private void validateFilterIds(
            InternshipAssignmentFilterRequest filter) {

        if (filter.getStudentId() != null
                && !studentRepository.existsById(filter.getStudentId())) {

            throw new NotFoundException(
                    "Không tìm thấy Student với ID = "
                            + filter.getStudentId());
        }

        if (filter.getMentorId() != null
                && !mentorRepository.existsById(filter.getMentorId())) {

            throw new NotFoundException(
                    "Không tìm thấy Mentor với ID = "
                            + filter.getMentorId());
        }

        if (filter.getPhaseId() != null
                && !internshipPhaseRepository.existsById(filter.getPhaseId())) {

            throw new NotFoundException(
                    "Không tìm thấy InternshipPhase với ID = "
                            + filter.getPhaseId());
        }
    }

    private void validateAssignedDate(
            InternshipAssignmentFilterRequest filter) {

        if (filter.getMinAssignedDate() != null
                && filter.getMaxAssignedDate() != null
                && filter.getMinAssignedDate()
                .isAfter(filter.getMaxAssignedDate())) {

            throw new BadRequestException(
                    "minAssignedDate không được sau maxAssignedDate");
        }
    }

    private Page<InternshipAssignment> getAllForAdmin(
            InternshipAssignmentFilterRequest filter,
            Pageable pageable) {

        if (filter.getStudentId() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentId(
                            filter.getStudentId(),
                            pageable);

        } else if (filter.getMentorId() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorId(
                            filter.getMentorId(),
                            pageable);

        } else if (filter.getPhaseId() != null) {

            return internshipAssignmentRepository
                    .findAllByPhase_PhaseId(
                            filter.getPhaseId(),
                            pageable);

        } else if (filter.getStatus() != null) {

            return internshipAssignmentRepository
                    .findAllByStatus(
                            filter.getStatus(),
                            pageable);

        } else if (filter.getAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByAssignedDate(
                            filter.getAssignedDate(),
                            pageable);

        } else if (filter.getMinAssignedDate() != null
                && filter.getMaxAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByAssignedDateBetween(
                            filter.getMinAssignedDate(),
                            filter.getMaxAssignedDate(),
                            pageable);

        } else if (filter.getMinAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByAssignedDateGreaterThanEqual(
                            filter.getMinAssignedDate(),
                            pageable);

        } else if (filter.getMaxAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByAssignedDateLessThanEqual(
                            filter.getMaxAssignedDate(),
                            pageable);

        } else if (filter.getStudentUsername() != null
                && !filter.getStudentUsername().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_User_UsernameContainingIgnoreCase(
                            filter.getStudentUsername(),
                            pageable);

        } else if (filter.getStudentFullName() != null
                && !filter.getStudentFullName().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_User_FullNameContainingIgnoreCase(
                            filter.getStudentFullName(),
                            pageable);

        } else if (filter.getStudentEmail() != null
                && !filter.getStudentEmail().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_User_EmailContainingIgnoreCase(
                            filter.getStudentEmail(),
                            pageable);

        } else if (filter.getStudentPhoneNumber() != null
                && !filter.getStudentPhoneNumber().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_User_PhoneNumberContainingIgnoreCase(
                            filter.getStudentPhoneNumber(),
                            pageable);

        } else if (filter.getMentorUsername() != null
                && !filter.getMentorUsername().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_User_UsernameContainingIgnoreCase(
                            filter.getMentorUsername(),
                            pageable);

        } else if (filter.getMentorFullName() != null
                && !filter.getMentorFullName().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_User_FullNameContainingIgnoreCase(
                            filter.getMentorFullName(),
                            pageable);

        } else if (filter.getMentorEmail() != null
                && !filter.getMentorEmail().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_User_EmailContainingIgnoreCase(
                            filter.getMentorEmail(),
                            pageable);

        } else if (filter.getMentorPhoneNumber() != null
                && !filter.getMentorPhoneNumber().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_User_PhoneNumberContainingIgnoreCase(
                            filter.getMentorPhoneNumber(),
                            pageable);
        }

        return internshipAssignmentRepository.findAll(pageable);
    }

    private Page<InternshipAssignment> getAllForMentor(
            InternshipAssignmentFilterRequest filter,
            Pageable pageable,
            User currentUser) {

        Mentor mentor = mentorRepository
                .findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role MENTOR"));

        Long mentorId = mentor.getMentorId();

        if (filter.getStudentId() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndStudent_StudentId(
                            mentorId,
                            filter.getStudentId(),
                            pageable);

        } else if (filter.getPhaseId() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndPhase_PhaseId(
                            mentorId,
                            filter.getPhaseId(),
                            pageable);

        } else if (filter.getStatus() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndStatus(
                            mentorId,
                            filter.getStatus(),
                            pageable);

        } else if (filter.getAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndAssignedDate(
                            mentorId,
                            filter.getAssignedDate(),
                            pageable);

        } else if (filter.getMinAssignedDate() != null
                && filter.getMaxAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndAssignedDateBetween(
                            mentorId,
                            filter.getMinAssignedDate(),
                            filter.getMaxAssignedDate(),
                            pageable);

        } else if (filter.getMinAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndAssignedDateGreaterThanEqual(
                            mentorId,
                            filter.getMinAssignedDate(),
                            pageable);

        } else if (filter.getMaxAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndAssignedDateLessThanEqual(
                            mentorId,
                            filter.getMaxAssignedDate(),
                            pageable);

        } else if (filter.getStudentUsername() != null
                && !filter.getStudentUsername().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndStudent_User_UsernameContainingIgnoreCase(
                            mentorId,
                            filter.getStudentUsername(),
                            pageable);

        } else if (filter.getStudentFullName() != null
                && !filter.getStudentFullName().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndStudent_User_FullNameContainingIgnoreCase(
                            mentorId,
                            filter.getStudentFullName(),
                            pageable);

        } else if (filter.getStudentEmail() != null
                && !filter.getStudentEmail().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndStudent_User_EmailContainingIgnoreCase(
                            mentorId,
                            filter.getStudentEmail(),
                            pageable);

        } else if (filter.getStudentPhoneNumber() != null
                && !filter.getStudentPhoneNumber().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndStudent_User_PhoneNumberContainingIgnoreCase(
                            mentorId,
                            filter.getStudentPhoneNumber(),
                            pageable);

        } else if (filter.getMentorUsername() != null
                && !filter.getMentorUsername().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndMentor_User_UsernameContainingIgnoreCase(
                            mentorId,
                            filter.getMentorUsername(),
                            pageable);

        } else if (filter.getMentorFullName() != null
                && !filter.getMentorFullName().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndMentor_User_FullNameContainingIgnoreCase(
                            mentorId,
                            filter.getMentorFullName(),
                            pageable);

        } else if (filter.getMentorEmail() != null
                && !filter.getMentorEmail().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndMentor_User_EmailContainingIgnoreCase(
                            mentorId,
                            filter.getMentorEmail(),
                            pageable);

        } else if (filter.getMentorPhoneNumber() != null
                && !filter.getMentorPhoneNumber().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByMentor_MentorIdAndMentor_User_PhoneNumberContainingIgnoreCase(
                            mentorId,
                            filter.getMentorPhoneNumber(),
                            pageable);
        }

        return internshipAssignmentRepository
                .findAllByMentor_MentorId(
                        mentorId,
                        pageable);
    }

    private Page<InternshipAssignment> getAllForStudent(
            InternshipAssignmentFilterRequest filter,
            Pageable pageable,
            User currentUser) {

        Student student = studentRepository
                .findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role STUDENT"));

        Long studentId = student.getStudentId();

        if (filter.getMentorId() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndMentor_MentorId(
                            studentId,
                            filter.getMentorId(),
                            pageable);

        } else if (filter.getPhaseId() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndPhase_PhaseId(
                            studentId,
                            filter.getPhaseId(),
                            pageable);

        } else if (filter.getStatus() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndStatus(
                            studentId,
                            filter.getStatus(),
                            pageable);

        } else if (filter.getAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndAssignedDate(
                            studentId,
                            filter.getAssignedDate(),
                            pageable);

        } else if (filter.getMinAssignedDate() != null
                && filter.getMaxAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndAssignedDateBetween(
                            studentId,
                            filter.getMinAssignedDate(),
                            filter.getMaxAssignedDate(),
                            pageable);

        } else if (filter.getMinAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndAssignedDateGreaterThanEqual(
                            studentId,
                            filter.getMinAssignedDate(),
                            pageable);

        } else if (filter.getMaxAssignedDate() != null) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndAssignedDateLessThanEqual(
                            studentId,
                            filter.getMaxAssignedDate(),
                            pageable);

        } else if (filter.getStudentUsername() != null
                && !filter.getStudentUsername().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndStudent_User_UsernameContainingIgnoreCase(
                            studentId,
                            filter.getStudentUsername(),
                            pageable);

        } else if (filter.getStudentFullName() != null
                && !filter.getStudentFullName().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndStudent_User_FullNameContainingIgnoreCase(
                            studentId,
                            filter.getStudentFullName(),
                            pageable);

        } else if (filter.getStudentEmail() != null
                && !filter.getStudentEmail().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndStudent_User_EmailContainingIgnoreCase(
                            studentId,
                            filter.getStudentEmail(),
                            pageable);

        } else if (filter.getStudentPhoneNumber() != null
                && !filter.getStudentPhoneNumber().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndStudent_User_PhoneNumberContainingIgnoreCase(
                            studentId,
                            filter.getStudentPhoneNumber(),
                            pageable);

        } else if (filter.getMentorUsername() != null
                && !filter.getMentorUsername().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndMentor_User_UsernameContainingIgnoreCase(
                            studentId,
                            filter.getMentorUsername(),
                            pageable);

        } else if (filter.getMentorFullName() != null
                && !filter.getMentorFullName().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndMentor_User_FullNameContainingIgnoreCase(
                            studentId,
                            filter.getMentorFullName(),
                            pageable);

        } else if (filter.getMentorEmail() != null
                && !filter.getMentorEmail().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndMentor_User_EmailContainingIgnoreCase(
                            studentId,
                            filter.getMentorEmail(),
                            pageable);

        } else if (filter.getMentorPhoneNumber() != null
                && !filter.getMentorPhoneNumber().isBlank()) {

            return internshipAssignmentRepository
                    .findAllByStudent_StudentIdAndMentor_User_PhoneNumberContainingIgnoreCase(
                            studentId,
                            filter.getMentorPhoneNumber(),
                            pageable);
        }

        return internshipAssignmentRepository
                .findAllByStudent_StudentId(
                        studentId,
                        pageable);
    }

    private InternshipAssignmentResponse toResponse(
            InternshipAssignment assignment) {

        return internshipAssignmentMapper.toResponse(assignment);
    }

    @Override
    public PaginationResponse<InternshipAssignmentResponse> getAllAssignments(
            InternshipAssignmentFilterRequest filter) {

        validateFilterIds(filter);

        validateAssignedDate(filter);

        User currentUser = getCurrentUser();

        Pageable pageable = PageRequest.of(
                filter.getPage() - 1,
                filter.getSize(),
                Sort.by("assignmentId").descending());

        Page<InternshipAssignment> assignmentPage;

        switch (currentUser.getRole()) {

            case ADMIN -> assignmentPage =
                    getAllForAdmin(filter, pageable);

            case MENTOR -> assignmentPage =
                    getAllForMentor(
                            filter,
                            pageable,
                            currentUser);

            case STUDENT -> assignmentPage =
                    getAllForStudent(
                            filter,
                            pageable,
                            currentUser);

            default -> throw new ForbiddenException(
                    "Không được phép truy cập");
        }

        List<InternshipAssignmentResponse> items =
                assignmentPage.getContent()
                        .stream()
                        .map(internshipAssignmentMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(filter.getPage())
                .pageSize(filter.getSize())
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

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {

                InternshipAssignment assignment =
                        internshipAssignmentRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy InternshipAssignment với ID = "
                                                        + id));

                return toResponse(assignment);
            }
            case MENTOR -> {

                Mentor mentor =
                        mentorRepository.findByUser_UserId(currentUser.getUserId())
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "User ID = "
                                                        + currentUser.getUserId()
                                                        + " chưa được liên kết với role MENTOR"));

                InternshipAssignment assignment =
                        internshipAssignmentRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy InternshipAssignment với ID = "
                                                        + id));

                Long ownerMentorId =
                        assignment.getMentor().getMentorId();

                if (!ownerMentorId.equals(mentor.getMentorId())) {

                    throw new ForbiddenException(
                            "FORBIDDEN MENTOR: không có quyền truy cập InternshipAssignment"
                                    + " | currentMentorId=" + mentor.getMentorId()
                                    + " | ownerMentorId=" + ownerMentorId
                                    + " | assignmentId=" + assignment.getAssignmentId());
                }

                return toResponse(assignment);
            }

            case STUDENT -> {

                Student student =
                        studentRepository.findByUser_UserId(currentUser.getUserId())
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "User ID = "
                                                        + currentUser.getUserId()
                                                        + " chưa được liên kết với role STUDENT"));

                InternshipAssignment assignment =
                        internshipAssignmentRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy InternshipAssignment với ID = "
                                                        + id));

                Long ownerStudentId =
                        assignment.getStudent().getStudentId();

                if (!ownerStudentId.equals(student.getStudentId())) {

                    throw new ForbiddenException(
                            "FORBIDDEN STUDENT: không có quyền truy cập InternshipAssignment"
                                    + " | currentStudentId=" + student.getStudentId()
                                    + " | ownerStudentId=" + ownerStudentId
                                    + " | assignmentId=" + assignment.getAssignmentId());
                }

                return toResponse(assignment);
            }
            default -> throw new ForbiddenException(
                    "Không có quyền truy cập InternshipAssignment");
        }
    }

    @Override
    public InternshipAssignmentResponse createAssignment(
            InternshipAssignmentCreateRequest request) {

        Student student =
                studentRepository.findById(request.getStudentId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy Student với ID = "
                                                + request.getStudentId()));

        Mentor mentor =
                mentorRepository.findById(request.getMentorId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy Mentor với ID = "
                                                + request.getMentorId()));

        InternshipPhase phase =
                internshipPhaseRepository.findById(request.getPhaseId())
                        .orElseThrow(() ->
                                new NotFoundException(
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

        assignment.setAssignedDate(LocalDateTime.now());

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
                                new NotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = " + id));

        boolean used =
                assignment.getAssessmentResults() != null
                        && !assignment.getAssessmentResults().isEmpty();

        if (used
                && request.getMentorId() != null && !assignment.getMentor()
                .getMentorId().equals(request.getMentorId())) {

            throw new ConflictException(
                    "Không thể cập nhật Mentor cho Assignment ID = "
                            + id
                            + " vì assignment đã liên kết với AssessmentResult");
        }

        if (request.getMentorId() != null) {

            Mentor mentor =
                    mentorRepository.findById(request.getMentorId())
                            .orElseThrow(() ->
                                    new NotFoundException(
                                            "Không tìm thấy Mentor với ID = "
                                                    + request.getMentorId()));

            assignment.setMentor(mentor);

            assignment.setAssignedDate(LocalDateTime.now());

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
                                new NotFoundException(
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
                                new NotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = " + id));

        if (assignment.getAssessmentResults() != null
                && !assignment.getAssessmentResults().isEmpty()) {

            throw new ConflictException(
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