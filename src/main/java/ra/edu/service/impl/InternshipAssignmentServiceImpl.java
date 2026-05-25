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
import ra.edu.repository.*;
import ra.edu.service.InternshipAssignmentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipAssignmentServiceImpl implements InternshipAssignmentService {

    private final InternshipAssignmentRepository internshipAssignmentRepository;

    private final UserRepository userRepository;

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

            userRepository.findById(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Student với ID = " + filter.getStudentId()));

            studentRepository.findByUser_UserId(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getStudentId() + " chưa được liên kết với role STUDENT"));

            return internshipAssignmentRepository
                    .findAllByStudent_StudentId(
                            filter.getStudentId(),
                            pageable);

        } else if (filter.getMentorId() != null) {

            userRepository.findById(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Mentor với ID = " + filter.getMentorId()));

            mentorRepository.findByUser_UserId(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getMentorId() + " chưa được liên kết với role MENTOR"));

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

            userRepository.findById(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Student với ID = " + filter.getStudentId()));

            studentRepository.findByUser_UserId(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getStudentId() + " chưa được liên kết với role STUDENT"));

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

            userRepository.findById(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Mentor với ID = " + filter.getMentorId()));

            mentorRepository.findByUser_UserId(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getMentorId() + " chưa được liên kết với role MENTOR"));

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

                    throw new NotFoundException(
                            "Không tìm thấy InternshipAssignment với ID = " + id);
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

                    throw new NotFoundException(
                            "Không tìm thấy InternshipAssignment với ID = " + id);
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

        userRepository.findById(request.getStudentId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Student với ID = " + request.getStudentId()));

        Student student =
                studentRepository.findByUser_UserId(request.getStudentId())
                        .orElseThrow(() -> new NotFoundException("User ID = " + request.getStudentId() + " chưa được liên kết với role STUDENT"));

        userRepository.findById(request.getMentorId())
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Mentor với ID = " + request.getMentorId()));

        Mentor mentor =
                mentorRepository.findByUser_UserId(request.getMentorId())
                        .orElseThrow(() -> new NotFoundException("User ID = " + request.getMentorId() + " chưa được liên kết với role MENTOR"));

        InternshipPhase phase =
                internshipPhaseRepository.findById(request.getPhaseId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy InternshipPhase với ID = "
                                                + request.getPhaseId()));

        LocalDate now = LocalDate.now();

        if (now.isBefore(phase.getStartDate())
                || now.isAfter(phase.getEndDate())) {

            throw new BadRequestException(
                    "Hiện tại không nằm trong thời gian của InternshipPhase ID = "
                            + phase.getPhaseId()
                            + " | startDate = "
                            + phase.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " | endDate = "
                            + phase.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (internshipAssignmentRepository
                .existsByStudent_StudentIdAndPhase_PhaseId(
                        request.getStudentId(),
                        request.getPhaseId())) {

            throw new ConflictException(
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

        LocalDate now = LocalDate.now();
        if (now.isBefore(assignment.getPhase().getStartDate()) || now.isAfter(assignment.getPhase().getEndDate())) {
            throw new BadRequestException("Không thể cập nhật. " +
                    "Hiện tại không nằm trong thời gian của InternshipPhase ID = "
                            + assignment.getPhase().getPhaseId()
                            + " | startDate = "
                            + assignment.getPhase().getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " | endDate = "
                            + assignment.getPhase().getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (assignment.getStatus() != InternshipAssignmentsStatus.PENDING) {

            throw new BadRequestException(
                    "Không thể cập nhật Assignment ID = "
                            + id
                            + " vì trạng thái hiện tại là " + assignment.getStatus()
                            + ". Chỉ ở trạng thái PENDING mới được cập nhật");
        }

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

            userRepository.findById(request.getMentorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Mentor với ID = " + request.getMentorId()));

            Mentor mentor =
                    mentorRepository.findByUser_UserId(request.getMentorId())
                            .orElseThrow(() -> new NotFoundException("User ID = " + request.getMentorId() + " chưa được liên kết với role MENTOR"));

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

        LocalDate now = LocalDate.now();
        if (now.isBefore(assignment.getPhase().getStartDate()) || now.isAfter(assignment.getPhase().getEndDate())) {
            throw new BadRequestException("Không thể cập nhật Status. " +
                    "Hiện tại không nằm trong thời gian của InternshipPhase ID = "
                    + assignment.getPhase().getPhaseId()
                    + " | startDate = "
                    + assignment.getPhase().getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " | endDate = "
                    + assignment.getPhase().getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        InternshipAssignmentsStatus oldStatus = assignment.getStatus();
        InternshipAssignmentsStatus newStatus = request.getStatus();

        if (oldStatus == newStatus) {
            return internshipAssignmentMapper.toResponse(assignment);
        }

        if (oldStatus == InternshipAssignmentsStatus.COMPLETED || oldStatus == InternshipAssignmentsStatus.CANCELLED) {
            throw new BadRequestException("Phân công thực tập này đã kết thúc với trạng thái " + oldStatus + ", không thể chỉnh sửa nữa!");
        }

        if (oldStatus == InternshipAssignmentsStatus.PENDING) {
            if (newStatus != InternshipAssignmentsStatus.IN_PROGRESS && newStatus != InternshipAssignmentsStatus.CANCELLED) {
                throw new BadRequestException("Từ trạng thái PENDING chỉ được chuyển sang IN_PROGRESS hoặc CANCELLED!");
            }
        }

        if (oldStatus == InternshipAssignmentsStatus.IN_PROGRESS) {
            if (newStatus != InternshipAssignmentsStatus.COMPLETED && newStatus != InternshipAssignmentsStatus.CANCELLED) {
                throw new BadRequestException("Từ trạng thái IN_PROGRESS chỉ được chuyển sang COMPLETED hoặc CANCELLED!");
            }
        }

        if (newStatus == InternshipAssignmentsStatus.COMPLETED) {
            boolean hasResult = assignment.getAssessmentResults() != null
                    && !assignment.getAssessmentResults().isEmpty();

            if (!hasResult) {
                throw new BadRequestException(
                        "Không thể sửa status thành COMPLETED. Phân công thực tập ID = " + id + " chưa có kết quả đánh giá!");
            }
        }

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

        LocalDate now = LocalDate.now();
        if (now.isBefore(assignment.getPhase().getStartDate()) || now.isAfter(assignment.getPhase().getEndDate())) {
            throw new BadRequestException("Không thể xóa. " +
                    "Hiện tại không nằm trong thời gian của InternshipPhase ID = "
                    + assignment.getPhase().getPhaseId()
                    + " | startDate = "
                    + assignment.getPhase().getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                    + " | endDate = "
                    + assignment.getPhase().getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (assignment.getStatus() != InternshipAssignmentsStatus.PENDING) {

            throw new BadRequestException(
                    "Không thể xóa Assignment ID = "
                            + id
                            + " vì trạng thái hiện tại là " + assignment.getStatus()
                            + ". Chỉ ở trạng thái PENDING mới được xoá");
        }

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