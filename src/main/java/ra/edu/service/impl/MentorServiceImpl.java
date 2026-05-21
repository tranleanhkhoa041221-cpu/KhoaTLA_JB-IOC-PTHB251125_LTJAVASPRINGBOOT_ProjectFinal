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
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorFilterRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.Mentor;
import ra.edu.entity.User;
import ra.edu.entity.UserRole;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
import ra.edu.mapper.MentorMapper;
import ra.edu.repository.InternshipAssignmentRepository;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.MentorService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorServiceImpl implements MentorService {

    private final MentorRepository mentorRepository;

    private final UserRepository userRepository;

    private final StudentRepository studentRepository;

    private final MentorMapper mentorMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<MentorResponse> getAllMentors(MentorFilterRequest filter) {

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.STUDENT) {

            studentRepository.findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "User ID = "
                                            + currentUser.getUserId()
                                            + " chưa được liên kết với role STUDENT"));
        }

        Pageable pageable = PageRequest.of(
                filter.getPage() - 1,
                filter.getSize(),
                Sort.by("mentorId").descending()
        );

        Page<Mentor> mentorPage;

        if (filter.getDepartment() != null && !filter.getDepartment().isBlank()
                && filter.getAcademicRank() != null && !filter.getAcademicRank().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByDepartmentContainingIgnoreCaseAndAcademicRankContainingIgnoreCase(
                            filter.getDepartment(),
                            filter.getAcademicRank(),
                            pageable
                    );

        } else if (filter.getDepartment() != null && !filter.getDepartment().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByDepartmentContainingIgnoreCase(
                            filter.getDepartment(),
                            pageable
                    );

        } else if (filter.getAcademicRank() != null && !filter.getAcademicRank().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByAcademicRankContainingIgnoreCase(
                            filter.getAcademicRank(),
                            pageable
                    );

        } else if (filter.getUsername() != null && !filter.getUsername().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByUser_UsernameContainingIgnoreCase(
                            filter.getUsername(),
                            pageable
                    );

        } else if (filter.getFullName() != null && !filter.getFullName().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByUser_FullNameContainingIgnoreCase(
                            filter.getFullName(),
                            pageable
                    );

        } else if (filter.getEmail() != null && !filter.getEmail().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByUser_EmailContainingIgnoreCase(
                            filter.getEmail(),
                            pageable
                    );

        } else if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByUser_PhoneNumberContainingIgnoreCase(
                            filter.getPhoneNumber(),
                            pageable
                    );

        } else {

            mentorPage = mentorRepository.findAll(pageable);
        }

        List<MentorResponse> items = mentorPage.getContent()
                .stream()
                .map(mentorMapper::toResponse)
                .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(filter.getPage())
                .pageSize(filter.getSize())
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

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {

                Mentor mentor =
                        mentorRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy Mentor với ID = " + id));

                return mentorMapper.toResponse(mentor);
            }

            case MENTOR -> {

                Mentor currentMentor =
                        mentorRepository.findByUser_UserId(currentUser.getUserId())
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "User ID = "
                                                        + currentUser.getUserId()
                                                        + " chưa được liên kết với role MENTOR"));

                Mentor mentor =
                        mentorRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy Mentor với ID = "
                                                        + id));

                if (!mentor.getMentorId()
                        .equals(currentMentor.getMentorId())) {

                    throw new ForbiddenException(
                            "Mentor ID = "
                                    + currentUser.getUserId()
                                    + " không có quyền xem Mentor ID = "
                                    + id
                                    + " (chỉ được xem thông tin của chính mình)");
                }

                return mentorMapper.toResponse(currentMentor);
            }

            case STUDENT -> {

                studentRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role STUDENT"));

                Mentor mentor =
                        mentorRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy Mentor với ID = " + id));

                return mentorMapper.toResponse(mentor);
            }

            default -> throw new ForbiddenException(
                    "Không có quyền truy cập Mentor");
        }
    }

    @Override
    public PaginationResponse<MentorResponse> getAssignedMentors(
            MentorFilterRequest filter) {

        User currentUser = getCurrentUser();

        studentRepository.findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role STUDENT"));

        Pageable pageable = PageRequest.of(
                filter.getPage() - 1,
                filter.getSize(),
                Sort.by("mentorId").descending()
        );

        Page<Mentor> mentorPage;

        if (filter.getDepartment() != null && !filter.getDepartment().isBlank()
                && filter.getAcademicRank() != null && !filter.getAcademicRank().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentIdAndDepartmentContainingIgnoreCaseAndAcademicRankContainingIgnoreCase(
                            currentUser.getUserId(),
                            filter.getDepartment(),
                            filter.getAcademicRank(),
                            pageable
                    );

        } else if (filter.getDepartment() != null && !filter.getDepartment().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentIdAndDepartmentContainingIgnoreCase(
                            currentUser.getUserId(),
                            filter.getDepartment(),
                            pageable
                    );

        } else if (filter.getAcademicRank() != null && !filter.getAcademicRank().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentIdAndAcademicRankContainingIgnoreCase(
                            currentUser.getUserId(),
                            filter.getAcademicRank(),
                            pageable
                    );
        } else if (filter.getUsername() != null && !filter.getUsername().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentIdAndUser_UsernameContainingIgnoreCase(
                            currentUser.getUserId(),
                            filter.getUsername(),
                            pageable
                    );

        } else if (filter.getFullName() != null && !filter.getFullName().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentIdAndUser_FullNameContainingIgnoreCase(
                            currentUser.getUserId(),
                            filter.getFullName(),
                            pageable
                    );

        } else if (filter.getEmail() != null && !filter.getEmail().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentIdAndUser_EmailContainingIgnoreCase(
                            currentUser.getUserId(),
                            filter.getEmail(),
                            pageable
                    );

        } else if (filter.getPhoneNumber() != null && !filter.getPhoneNumber().isBlank()) {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentIdAndUser_PhoneNumberContainingIgnoreCase(
                            currentUser.getUserId(),
                            filter.getPhoneNumber(),
                            pageable
                    );


        } else {

            mentorPage = mentorRepository
                    .findAllByInternshipAssignments_Student_StudentId(
                            currentUser.getUserId(),
                            pageable
                    );
        }

        List<MentorResponse> items = mentorPage.getContent()
                .stream()
                .map(mentorMapper::toResponse)
                .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(filter.getPage())
                .pageSize(filter.getSize())
                .totalPages(mentorPage.getTotalPages())
                .totalItems(mentorPage.getTotalElements())
                .build();

        return PaginationResponse.<MentorResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public MentorResponse getAssignedMentorById(Long mentorId) {

        User currentUser = getCurrentUser();

        studentRepository.findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role STUDENT"));

        Mentor mentor =
                mentorRepository.findById(mentorId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy Mentor với ID = "
                                                + mentorId));


        boolean assigned =
                mentorRepository.existsByInternshipAssignments_Student_StudentIdAndMentorId(
                        currentUser.getUserId(), mentorId);

        if (!assigned) {

            throw new ForbiddenException(
                    "Student ID = "
                            + currentUser.getUserId()
                            + " không được phân công cho Mentor ID = "
                            + mentorId);
        }

        return mentorMapper.toResponse(mentor);
    }

    @Override
    public MentorResponse createMentor(MentorCreateRequest request) {

        User user =
                userRepository.findById(request.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + request.getUserId()));

        if (user.getRole() != UserRole.MENTOR) {

            throw new ConflictException(
                    "User ID = "
                            + user.getUserId()
                            + " phải có role MENTOR mới được liên kết");
        }

        if (mentorRepository.existsByUser_UserId(user.getUserId())) {

            throw new ConflictException(
                    "User ID = "
                            + user.getUserId()
                            + " đã liên kết với Mentor");
        }

        Mentor mentor = mentorMapper.toEntity(request);

        mentor.setUser(user);

        mentor.setCreatedAt(LocalDateTime.now());

        mentor.setUpdatedAt(LocalDateTime.now());

        mentorRepository.save(mentor);

        return mentorMapper.toResponse(mentor);
    }

    @Override
    public MentorResponse updateMentor(Long id, MentorUpdateRequest request) {

        Mentor mentor =
                mentorRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy Mentor với ID = " + id));

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.MENTOR) {

            Mentor currentMentor =
                    mentorRepository.findByUser_UserId(currentUser.getUserId())
                            .orElseThrow(() ->
                                    new NotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role MENTOR"));

            if (!currentMentor.getMentorId().equals(id)) {


                throw new ForbiddenException(
                        "Mentor ID = "
                                + currentUser.getUserId()
                                + " không có quyền cập nhật Mentor ID = "
                                + id
                                + " (chỉ được cập nhật thông tin của chính mình)");
            }
        }

        mentorMapper.updateEntityFromDto(request, mentor);

        mentor.setUpdatedAt(LocalDateTime.now());

        mentorRepository.save(mentor);

        return mentorMapper.toResponse(mentor);
    }
}
