package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.access.AccessDeniedException;
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
import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.StudentResponse;
import ra.edu.entity.Student;
import ra.edu.entity.User;
import ra.edu.entity.UserRole;
import ra.edu.mapper.StudentMapper;
import ra.edu.repository.StudentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.StudentService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final StudentMapper studentMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }


    @Override
    public PaginationResponse<StudentResponse> getAllStudents(
            int page,
            int size,
            String studentCode,
            String address,
            LocalDate dateOfBirth,
            String username,
            String fullName,
            String email,
            String phoneNumber,
            String major,
            String className
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
                Sort.by("studentId").descending()
        );

        Page<Student> studentPage;


        if (currentUser.getRole() == UserRole.ADMIN) {

            if (studentCode != null && !studentCode.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByStudentCodeContainingIgnoreCase(
                                        studentCode,
                                        pageable
                                );
            } else if (address != null && !address.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAddressContainingIgnoreCase(
                                        address,
                                        pageable
                                );
            } else if (dateOfBirth != null) {

                studentPage =
                        studentRepository
                                .findAllByDateOfBirth(
                                        dateOfBirth,
                                        pageable
                                );
            } else if (username != null && !username.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByUser_UsernameContainingIgnoreCase(
                                        username,
                                        pageable
                                );
            } else if (fullName != null && !fullName.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByUser_FullNameContainingIgnoreCase(
                                        fullName,
                                        pageable
                                );
            } else if (email != null && !email.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByUser_EmailContainingIgnoreCase(
                                        email,
                                        pageable
                                );
            } else if (phoneNumber != null && !phoneNumber.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByUser_PhoneNumberContainingIgnoreCase(
                                        phoneNumber,
                                        pageable
                                );
            } else if (major != null && !major.isBlank()
                    && className != null && !className.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByMajorContainingIgnoreCaseAndClassNameContainingIgnoreCase(
                                        major,
                                        className,
                                        pageable
                                );
            } else if (major != null && !major.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByMajorContainingIgnoreCase(
                                        major,
                                        pageable
                                );
            } else if (className != null && !className.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByClassNameContainingIgnoreCase(
                                        className,
                                        pageable
                                );
            } else {

                studentPage = studentRepository.findAll(pageable);
            }
        } else {
            if (studentCode != null && !studentCode.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndStudentCodeContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        studentCode,
                                        pageable
                                );
            } else if (address != null && !address.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndAddressContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        address,
                                        pageable
                                );
            } else if (dateOfBirth != null) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndDateOfBirth(
                                        currentUser.getUserId(),
                                        dateOfBirth,
                                        pageable
                                );
            } else if (username != null && !username.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndUser_UsernameContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        username,
                                        pageable
                                );
            } else if (fullName != null && !fullName.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndUser_FullNameContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        fullName,
                                        pageable
                                );
            } else if (email != null && !email.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndUser_EmailContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        email,
                                        pageable
                                );
            } else if (phoneNumber != null && !phoneNumber.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndUser_PhoneNumberContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        phoneNumber,
                                        pageable
                                );
            } else if (major != null && !major.isBlank()
                    && className != null && !className.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndMajorContainingIgnoreCaseAndClassNameContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        major,
                                        className,
                                        pageable
                                );
            } else if (major != null && !major.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndMajorContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        major,
                                        pageable
                                );
            } else if (className != null && !className.isBlank()) {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorIdAndClassNameContainingIgnoreCase(
                                        currentUser.getUserId(),
                                        className,
                                        pageable
                                );
            } else {

                studentPage =
                        studentRepository
                                .findAllByAssignments_Mentor_MentorId(
                                        currentUser.getUserId(),
                                        pageable
                                );
            }
        }

        List<StudentResponse> items =
                studentPage.getContent()
                        .stream()
                        .map(studentMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(studentPage.getTotalPages())
                .totalItems(studentPage.getTotalElements())
                .build();

        return PaginationResponse.<StudentResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }


    @Override
    public StudentResponse getStudentById(Long id) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Student với ID = " + id
                                )
                        );

        User currentUser = getCurrentUser();


        if (currentUser.getRole() == UserRole.ADMIN) {

            return studentMapper.toResponse(student);
        }


        if (currentUser.getRole() == UserRole.MENTOR) {

            boolean assigned =
                    studentRepository.existsByAssignments_Mentor_MentorIdAndStudentId(
                            currentUser.getUserId(),
                            id
                    );

            if (!assigned) {

                throw new AccessDeniedException(
                        "Mentor ID = "
                                + currentUser.getUserId()
                                + " không được phân công hướng dẫn Student ID = "
                                + id
                );
            }

            return studentMapper.toResponse(student);
        }

        if (!student.getUser().getUserId()
                .equals(currentUser.getUserId())) {

            throw new AccessDeniedException(
                    "Student ID = "
                            + currentUser.getUserId()
                            + " không có quyền xem Student ID = "
                            + id
                            + " (chỉ được xem thông tin của chính mình)"
            );
        }

        return studentMapper.toResponse(student);
    }


    @Override
    public StudentResponse createStudent(
            StudentCreateRequest request
    ) {

        if (studentRepository.existsByStudentCode(
                request.getStudentCode()
        )) {

            throw new IllegalStateException(
                    "Mã sinh viên đã tồn tại"
            );
        }

        User user =
                userRepository.findById(request.getUserId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + request.getUserId()
                                )
                        );

        if (user.getRole() != UserRole.STUDENT) {

            throw new IllegalStateException(
                    "User ID = "
                            + user.getUserId()
                            + " phải có role STUDENT mới được liên kết"
            );
        }

        if (studentRepository.existsByUser_UserId(
                user.getUserId()
        )) {

            throw new IllegalStateException(
                    "User ID = "
                            + user.getUserId()
                            + " đã liên kết với Student"
            );
        }

        Student student = studentMapper.toEntity(request);

        student.setUser(user);

        student.setCreatedAt(LocalDateTime.now());

        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(student);

        return studentMapper.toResponse(student);
    }


    @Override
    public StudentResponse updateStudent(
            Long id,
            StudentUpdateRequest request
    ) {

        Student student =
                studentRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Student với ID = " + id
                                )
                        );

        User currentUser = getCurrentUser();


        if (currentUser.getRole() == UserRole.STUDENT) {

            if (!student.getUser().getUserId()
                    .equals(currentUser.getUserId())) {

                throw new AccessDeniedException(
                        "Student ID = "
                                + currentUser.getUserId()
                                + " không có quyền cập nhật Student ID = "
                                + id
                                + " (chỉ được cập nhật thông tin của chính mình)"
                );
            }
        }

        studentMapper.updateEntityFromDto(
                request,
                student
        );

        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(student);

        return studentMapper.toResponse(student);
    }
}