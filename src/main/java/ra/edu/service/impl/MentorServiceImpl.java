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
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.InternshipAssignment;
import ra.edu.entity.Mentor;
import ra.edu.entity.User;
import ra.edu.entity.UserRole;
import ra.edu.mapper.MentorMapper;
import ra.edu.repository.InternshipAssignmentRepository;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.MentorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    private final UserRepository userRepository;

    private final InternshipAssignmentRepository internshipAssignmentRepository;

    private final MentorMapper mentorMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<MentorResponse> getAllMentors(
            int page,
            int size,
            String username,
            String fullName,
            String email,
            String phoneNumber,
            String department,
            String academicRank
    ) {

        if (page < 1) {
            throw new IllegalArgumentException("Page phải >= 1");
        }

        if (size < 1) {
            throw new IllegalArgumentException("Size phải >= 1");
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("mentorId").descending()
        );

        Page<Mentor> mentorPage;

        if (department != null && !department.isBlank()
                && academicRank != null && !academicRank.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByDepartmentContainingIgnoreCaseAndAcademicRankContainingIgnoreCase(
                                    department,
                                    academicRank,
                                    pageable
                            );

        } else if (department != null && !department.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByDepartmentContainingIgnoreCase(
                                    department,
                                    pageable
                            );

        } else if (academicRank != null && !academicRank.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAcademicRankContainingIgnoreCase(
                                    academicRank,
                                    pageable
                            );

        } else if (username != null && !username.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByUser_UsernameContainingIgnoreCase(
                                    username,
                                    pageable
                            );
        } else if (fullName != null && !fullName.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByUser_FullNameContainingIgnoreCase(
                                    fullName,
                                    pageable
                            );
        } else if (email != null && !email.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByUser_EmailContainingIgnoreCase(
                                    email,
                                    pageable
                            );
        } else if (phoneNumber != null && !phoneNumber.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByUser_PhoneNumberContainingIgnoreCase(
                                    phoneNumber,
                                    pageable
                            );
        } else {

            mentorPage = mentorRepository.findAll(pageable);
        }

        List<MentorResponse> items =
                mentorPage.getContent()
                        .stream()
                        .map(mentorMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(mentorPage.getTotalPages())
                .totalItems(mentorPage.getTotalElements())
                .build();

        return PaginationResponse.<MentorResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public MentorResponse getMentorById(Long id) {

        Mentor mentor =
                mentorRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Mentor với ID = " + id
                                )
                        );

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.MENTOR) {

            if (!mentor.getUser().getUserId()
                    .equals(currentUser.getUserId())) {

                throw new AccessDeniedException(
                        "Mentor ID = "
                                + currentUser.getUserId()
                                + " không có quyền xem Mentor ID = "
                                + id
                                + " (chỉ được xem thông tin của chính mình)"
                );
            }
        }

        return mentorMapper.toResponse(mentor);
    }

    @Override
    public PaginationResponse<MentorResponse> getAssignedMentors(
            int page,
            int size,
            String username,
            String fullName,
            String email,
            String phoneNumber,
            String department,
            String academicRank
    ) {

        if (page < 1) {
            throw new IllegalArgumentException("Page phải >= 1");
        }

        if (size < 1) {
            throw new IllegalArgumentException("Size phải >= 1");
        }

        User currentUser = getCurrentUser();

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("mentorId").descending()
        );

        Page<Mentor> mentorPage;

        if (department != null && !department.isBlank()
                && academicRank != null && !academicRank.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentIdAndDepartmentContainingIgnoreCaseAndAcademicRankContainingIgnoreCase(
                                    currentUser.getUserId(),
                                    department,
                                    academicRank,
                                    pageable
                            );
        } else if (department != null && !department.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentIdAndDepartmentContainingIgnoreCase(
                                    currentUser.getUserId(),
                                    department,
                                    pageable
                            );
        } else if (academicRank != null && !academicRank.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentIdAndAcademicRankContainingIgnoreCase(
                                    currentUser.getUserId(),
                                    academicRank,
                                    pageable
                            );
        } else if (username != null && !username.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentIdAndUser_UsernameContainingIgnoreCase(
                                    currentUser.getUserId(),
                                    username,
                                    pageable
                            );
        } else if (fullName != null && !fullName.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentIdAndUser_FullNameContainingIgnoreCase(
                                    currentUser.getUserId(),
                                    fullName,
                                    pageable
                            );
        } else if (email != null && !email.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentIdAndUser_EmailContainingIgnoreCase(
                                    currentUser.getUserId(),
                                    email,
                                    pageable
                            );
        } else if (phoneNumber != null && !phoneNumber.isBlank()) {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentIdAndUser_PhoneNumberContainingIgnoreCase(
                                    currentUser.getUserId(),
                                    phoneNumber,
                                    pageable
                            );
        } else {

            mentorPage =
                    mentorRepository
                            .findAllByAssignments_Student_StudentId(
                                    currentUser.getUserId(), pageable);
        }

        List<MentorResponse> items =
                mentorPage.getContent()
                        .stream()
                        .map(mentorMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(mentorPage.getTotalPages())
                .totalItems(mentorPage.getTotalElements())
                .build();

        return PaginationResponse.<MentorResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public MentorResponse getAssignedMentorById(
            Long mentorId
    ) {

        Mentor mentor =
                mentorRepository.findById(mentorId)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Mentor với ID = "
                                                + mentorId
                                )
                        );

        User currentUser = getCurrentUser();

        boolean assigned =
                mentorRepository.existsByAssignments_Student_StudentIdAndMentorId(
                        currentUser.getUserId(),
                        mentorId
                );

        if (!assigned) {

            throw new AccessDeniedException(
                    "Student ID = "
                            + currentUser.getUserId()
                            + " không được phân công cho Mentor ID = "
                            + mentorId
            );
        }

        return mentorMapper.toResponse(mentor);
    }

    @Override
    public MentorResponse createMentor(
            MentorCreateRequest request
    ) {

        User user =
                userRepository.findById(request.getUserId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + request.getUserId()
                                )
                        );

        if (user.getRole() != UserRole.MENTOR) {

            throw new IllegalStateException(
                    "User ID = "
                            + user.getUserId()
                            + " phải có role MENTOR mới được liên kết"
            );
        }

        if (mentorRepository.existsByUser_UserId(
                user.getUserId()
        )) {

            throw new IllegalStateException(
                    "User ID = "
                            + user.getUserId()
                            + " đã liên kết với Mentor"
            );
        }

        Mentor mentor = mentorMapper.toEntity(request);

        mentor.setUser(user);

        mentor.setCreatedAt(LocalDateTime.now());

        mentor.setUpdatedAt(LocalDateTime.now());

        mentorRepository.save(mentor);

        return mentorMapper.toResponse(mentor);
    }

    @Override
    public MentorResponse updateMentor(
            Long id,
            MentorUpdateRequest request
    ) {

        Mentor mentor =
                mentorRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Mentor với ID = " + id));

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.MENTOR) {

            if (!mentor.getUser().getUserId()
                    .equals(currentUser.getUserId())) {

                throw new AccessDeniedException(
                        "Mentor ID = "
                                + currentUser.getUserId()
                                + " không có quyền cập nhật Mentor ID = "
                                + id
                                + " (chỉ được cập nhật thông tin của chính mình)"
                );
            }
        }

        mentorMapper.updateEntityFromDto(
                request,
                mentor
        );

        mentor.setUpdatedAt(LocalDateTime.now());

        mentorRepository.save(mentor);

        return mentorMapper.toResponse(mentor);
    }
}
