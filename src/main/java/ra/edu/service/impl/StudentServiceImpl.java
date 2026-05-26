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
import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentFilterRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.StudentResponse;
import ra.edu.entity.Mentor;
import ra.edu.entity.Student;
import ra.edu.entity.User;
import ra.edu.entity.UserRole;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
import ra.edu.mapper.StudentMapper;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.StudentService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;

    private final UserRepository userRepository;

    private final MentorRepository mentorRepository;

    private final StudentMapper studentMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    private Page<Student> getAllForAdmin(
            StudentFilterRequest filter,
            Pageable pageable) {

        if (filter.getStudentCode() != null
                && !filter.getStudentCode().isBlank()) {

            return studentRepository
                    .findAllByStudentCodeContainingIgnoreCase(
                            filter.getStudentCode(),
                            pageable);

        } else if (filter.getAddress() != null
                && !filter.getAddress().isBlank()) {

            return studentRepository
                    .findAllByAddressContainingIgnoreCase(
                            filter.getAddress(),
                            pageable);

        } else if (filter.getDateOfBirth() != null) {

            return studentRepository
                    .findAllByDateOfBirth(
                            filter.getDateOfBirth(),
                            pageable);

        } else if (filter.getUsername() != null
                && !filter.getUsername().isBlank()) {

            return studentRepository
                    .findAllByUser_UsernameContainingIgnoreCase(
                            filter.getUsername(),
                            pageable);

        } else if (filter.getFullName() != null
                && !filter.getFullName().isBlank()) {

            return studentRepository
                    .findAllByUser_FullNameContainingIgnoreCase(
                            filter.getFullName(),
                            pageable);

        } else if (filter.getEmail() != null
                && !filter.getEmail().isBlank()) {

            return studentRepository
                    .findAllByUser_EmailContainingIgnoreCase(
                            filter.getEmail(),
                            pageable);

        } else if (filter.getPhoneNumber() != null
                && !filter.getPhoneNumber().isBlank()) {

            return studentRepository
                    .findAllByUser_PhoneNumberContainingIgnoreCase(
                            filter.getPhoneNumber(),
                            pageable);

        } else if (filter.getMajor() != null
                && !filter.getMajor().isBlank()
                && filter.getClassName() != null
                && !filter.getClassName().isBlank()) {

            return studentRepository
                    .findAllByMajorContainingIgnoreCaseAndClassNameContainingIgnoreCase(
                            filter.getMajor(),
                            filter.getClassName(),
                            pageable);

        } else if (filter.getMajor() != null
                && !filter.getMajor().isBlank()) {

            return studentRepository
                    .findAllByMajorContainingIgnoreCase(
                            filter.getMajor(),
                            pageable);

        } else if (filter.getClassName() != null
                && !filter.getClassName().isBlank()) {

            return studentRepository
                    .findAllByClassNameContainingIgnoreCase(
                            filter.getClassName(),
                            pageable);
        }

        return studentRepository.findAll(pageable);
    }

    private Page<Student> getAllForMentor(
            StudentFilterRequest filter,
            Pageable pageable,
            User currentUser) {

        Mentor mentor =
                mentorRepository.findByUser_UserId(
                                currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role MENTOR"));

        Long mentorId = mentor.getMentorId();

        if (filter.getStudentCode() != null
                && !filter.getStudentCode().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndStudentCodeContainingIgnoreCase(
                            mentorId,
                            filter.getStudentCode(),
                            pageable);

        } else if (filter.getAddress() != null
                && !filter.getAddress().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndAddressContainingIgnoreCase(
                            mentorId,
                            filter.getAddress(),
                            pageable);

        } else if (filter.getDateOfBirth() != null) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndDateOfBirth(
                            mentorId,
                            filter.getDateOfBirth(),
                            pageable);

        } else if (filter.getUsername() != null
                && !filter.getUsername().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndUser_UsernameContainingIgnoreCase(
                            mentorId,
                            filter.getUsername(),
                            pageable);

        } else if (filter.getFullName() != null
                && !filter.getFullName().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndUser_FullNameContainingIgnoreCase(
                            mentorId,
                            filter.getFullName(),
                            pageable);

        } else if (filter.getEmail() != null
                && !filter.getEmail().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndUser_EmailContainingIgnoreCase(
                            mentorId,
                            filter.getEmail(),
                            pageable);

        } else if (filter.getPhoneNumber() != null
                && !filter.getPhoneNumber().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndUser_PhoneNumberContainingIgnoreCase(
                            mentorId,
                            filter.getPhoneNumber(),
                            pageable);

        } else if (filter.getMajor() != null
                && !filter.getMajor().isBlank()
                && filter.getClassName() != null
                && !filter.getClassName().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndMajorContainingIgnoreCaseAndClassNameContainingIgnoreCase(
                            mentorId,
                            filter.getMajor(),
                            filter.getClassName(),
                            pageable);

        } else if (filter.getMajor() != null
                && !filter.getMajor().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndMajorContainingIgnoreCase(
                            mentorId,
                            filter.getMajor(),
                            pageable);

        } else if (filter.getClassName() != null
                && !filter.getClassName().isBlank()) {

            return studentRepository
                    .findAllByInternshipAssignments_Mentor_MentorIdAndClassNameContainingIgnoreCase(
                            mentorId,
                            filter.getClassName(),
                            pageable);
        }

        return studentRepository
                .findAllByInternshipAssignments_Mentor_MentorId(
                        mentorId,
                        pageable);
    }


    @Override
    public PaginationResponse<StudentResponse> getAllStudents(
            StudentFilterRequest filter) {

        Pageable pageable = PageRequest.of(
                filter.getPage() - 1,
                filter.getSize(),
                Sort.by("studentId").descending());

        User currentUser = getCurrentUser();

        Page<Student> studentPage;

        switch (currentUser.getRole()) {

            case ADMIN -> studentPage = getAllForAdmin(
                    filter,
                    pageable);

            case MENTOR -> studentPage = getAllForMentor(
                    filter,
                    pageable,
                    currentUser);

            default -> throw new ForbiddenException(
                    "Không có quyền truy cập danh sách Student");
        }


        List<StudentResponse> items =
                studentPage.getContent()
                        .stream()
                        .map(studentMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(filter.getPage())
                .pageSize(filter.getSize())
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

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Student với ID = " + id));

        Student student = studentRepository.findByUser_UserId(id)
                .orElseThrow(() -> new NotFoundException("User ID = " + id + " chưa được liên kết với role STUDENT"));

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {

                return studentMapper.toResponse(student);
            }

            case MENTOR -> {

                Mentor currentMentor = mentorRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() -> new NotFoundException("User ID = " + currentUser.getUserId() + " chưa được liên kết với role MENTOR"));

                boolean assigned = studentRepository.existsByInternshipAssignments_Mentor_MentorIdAndStudentId(
                        currentMentor.getMentorId(), student.getStudentId());

                if (!assigned) {

                    throw new NotFoundException("Không tìm thấy Student với ID = " + id);
                }

                return studentMapper.toResponse(student);
            }

            case STUDENT -> {

                Student currentStudent = studentRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() -> new NotFoundException("User ID = " + currentUser.getUserId() + " chưa được liên kết với role STUDENT"));

                if (!student.getStudentId().equals(currentStudent.getStudentId())) {

                    throw new NotFoundException("Không tìm thấy Student với ID = " + id);
                }

                return studentMapper.toResponse(student);
            }

            default -> throw new ForbiddenException("Không có quyền truy cập");
        }
    }

    @Override
    public StudentResponse createStudent(StudentCreateRequest request) {

        if (studentRepository.existsByStudentCodeIgnoreCase(
                request.getStudentCode())) {

            throw new ConflictException("Mã sinh viên đã tồn tại");
        }

        User user =
                userRepository.findById(request.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + request.getUserId()));

        if (user.getRole() != UserRole.STUDENT) {

            throw new ConflictException(
                    "User ID = "
                            + user.getUserId()
                            + " phải có role STUDENT mới được liên kết");
        }

        if (studentRepository.existsByUser_UserId(
                user.getUserId())) {

            throw new ConflictException(
                    "User ID = "
                            + user.getUserId()
                            + " đã liên kết với Student");
        }

        Student student = studentMapper.toEntity(request);

        student.setUser(user);

        student.setCreatedAt(LocalDateTime.now());

        studentRepository.save(student);

        return studentMapper.toResponse(student);
    }

    @Override
    public StudentResponse updateStudent(Long id, StudentUpdateRequest request) {

        userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Student với ID = " + id));

        Student student = studentRepository.findByUser_UserId(id)
                .orElseThrow(() -> new NotFoundException("User ID = " + id + " chưa được liên kết với role STUDENT"));


        User currentUser = getCurrentUser();


        if (currentUser.getRole() == UserRole.STUDENT) {

            Student currentStudent =
                    studentRepository.findByUser_UserId(
                                    currentUser.getUserId())
                            .orElseThrow(() ->
                                    new NotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role STUDENT"));


            if (!currentStudent.getStudentId()
                    .equals(student.getStudentId())) {

                throw new NotFoundException(
                        "Không tìm thấy Student với ID = " + id);
            }
        }

        if (request.getMajor() != null) {
            request.setMajor(request.getMajor().trim());
        }
        if (request.getClassName() != null) {
            request.setClassName(request.getClassName().trim());
        }
        if (request.getAddress() != null) {
            request.setAddress(request.getAddress().trim());
        }

        boolean hasChanges = false;

        if (request.getMajor() != null && !request.getMajor().equalsIgnoreCase(student.getMajor())) {
            hasChanges = true;
        }
        if (request.getClassName() != null && !request.getClassName().equalsIgnoreCase(student.getClassName())) {
            hasChanges = true;
        }
        if (request.getAddress() != null && !request.getAddress().equalsIgnoreCase(student.getAddress())) {
            hasChanges = true;
        }

        if (request.getDateOfBirth() != null && !request.getDateOfBirth().equals(student.getDateOfBirth())) {
            hasChanges = true;
        }

        if (!hasChanges) {
            return null;
        }

        studentMapper.updateEntityFromDto(request, student);

        student.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(student);

        return studentMapper.toResponse(student);
    }
}