package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.request.UserUpdateRoleRequest;
import ra.edu.dto.request.UserUpdateStatusRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.UserResponse;
import ra.edu.entity.User;
import ra.edu.entity.UserRole;
import ra.edu.mapper.UserMapper;
import ra.edu.repository.AssessmentResultRepository;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.repository.UserRepository;
import ra.edu.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    private final StudentRepository studentRepository;

    private final MentorRepository mentorRepository;

    private final AssessmentResultRepository assessmentResultRepository;

    @Override
    public PaginationResponse<UserResponse> getAllUsers
            (int page,
             int size,
             String username,
             String fullName,
             String email,
             String phoneNumber,
             String isActive,
             UserRole role) {

        Boolean active = null;

        if (isActive != null && !isActive.isBlank()) {

            if (!isActive.equalsIgnoreCase("true")
                    && !isActive.equalsIgnoreCase("false")) {

                throw new IllegalArgumentException(
                        "isActive không hợp lệ. Chỉ được true hoặc false"
                );
            }

            active = Boolean.parseBoolean(isActive);
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("userId").ascending());

        Page<User> userPage;

        if (username != null && !username.isBlank()) {

            userPage = userRepository.findAllByUsernameContainingIgnoreCase(username, pageable);

        } else if (fullName != null && !fullName.isBlank()) {

            userPage = userRepository.findAllByFullNameContainingIgnoreCase(fullName, pageable);

        } else if (email != null && !email.isBlank()) {

            userPage = userRepository.findAllByEmailContainingIgnoreCase(email, pageable);

        } else if (phoneNumber != null && !phoneNumber.isBlank()) {

            userPage = userRepository.findAllByPhoneNumberContainingIgnoreCase(phoneNumber, pageable);

        } else if (active != null) {

            userPage = userRepository.findAllByIsActive(active, pageable);

        } else if (role != null) {

            userPage = userRepository.findAllByRole(role, pageable);

        } else {

            userPage = userRepository.findAll(pageable);

        }

        List<UserResponse> items = userPage.getContent()
                .stream()
                .map(userMapper::toResponse)
                .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(userPage.getTotalPages())
                .totalItems(userPage.getTotalElements())
                .build();

        return PaginationResponse.<UserResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID = " + id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByUsernameIgnoreCase(request.getUsername()))

            throw new IllegalStateException("Username đã tồn tại");

        if (userRepository.existsByEmailIgnoreCase(request.getEmail()))

            throw new IllegalStateException("Email đã tồn tại");

        if (userRepository.existsByPhoneNumber(request.getPhoneNumber()))

            throw new IllegalStateException("Số điện thoại đã tồn tại");

        User user = userMapper.toEntity(request);

        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserUpdateRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID = " + id));


        if (!user.getUsername().equalsIgnoreCase(request.getUsername()) && userRepository.existsByUsernameIgnoreCase(request.getUsername())) {

            throw new IllegalStateException("Username đã tồn tại");
        }

        if (!user.getEmail().equalsIgnoreCase(request.getEmail()) && userRepository.existsByEmailIgnoreCase(request.getEmail())) {

            throw new IllegalStateException("Email đã tồn tại");
        }

        if (!user.getPhoneNumber().equalsIgnoreCase(request.getPhoneNumber()) && userRepository.existsByPhoneNumber(request.getPhoneNumber())) {

            throw new IllegalStateException("Số điện thoại đã tồn tại");
        }

        userMapper.updateEntityFromDto(request, user);

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserStatus(Long id, UserUpdateStatusRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID = " + id));

        if (studentRepository.existsByUser_UserId(user.getUserId())) {

            throw new IllegalStateException(
                    "Không thể thay đổi trạng thái User ID = " + user.getUserId() + " vì đã liên kết với Student");
        }

        if (mentorRepository.existsByUser_UserId(user.getUserId())) {

            throw new IllegalStateException(
                    "Không thể thay đổi trạng thái User ID = " + user.getUserId() + " vì đã liên kết với Mentor");
        }

        if (assessmentResultRepository.existsByEvaluatedBy_UserId(user.getUserId())) {

            throw new IllegalStateException(
                    "Không thể thay đổi trạng thái User ID = " + user.getUserId() + " vì đã liên kết với AssessmentResult");
        }

        user.setIsActive(Boolean.parseBoolean(request.getIsActive()));
//        user.setIsActive(request.getIsActive());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserRole(Long id, UserUpdateRoleRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID = " + id));

        if (user.getRole() == UserRole.ADMIN && !user.getUserId().equals(id)) {

            throw new IllegalStateException(
                    "ADMIN không được phép thay đổi vai trò của ADMIN khác");
        }

        if (studentRepository.existsByUser_UserId(user.getUserId())) {

            throw new IllegalStateException(
                    "Không thể thay đổi role của User ID = " + user.getUserId() + " vì đã liên kết với Student");
        }

        if (mentorRepository.existsByUser_UserId(user.getUserId())) {

            throw new IllegalStateException(
                    "Không thể thay đổi role của User ID = " + user.getUserId() + " vì đã liên kết với Mentor");
        }

        if (assessmentResultRepository.existsByEvaluatedBy_UserId(user.getUserId())) {
            throw new IllegalStateException(
                    "Không thể thay đổi role của User ID = " + user.getUserId() + " vì đã liên kết với AssessmentResult");
        }

//        user.setRole(UserRole.valueOf(request.getRole()));
        user.setRole(request.getRole());
        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy người dùng với ID = " + id));

        if (user.getRole() == UserRole.ADMIN && !user.getUserId().equals(id)) {

            throw new IllegalStateException("ADMIN không được phép xóa tài khoản ADMIN khác");
        }

        if (studentRepository.existsByUser_UserId(user.getUserId())) {

            throw new IllegalStateException(
                    "Không thể xóa User ID = " + user.getUserId() + " vì đã liên kết với Student");
        }

        if (mentorRepository.existsByUser_UserId(user.getUserId())) {
            throw new IllegalStateException(
                    "Không thể xóa User ID = " + user.getUserId() + " vì đã liên kết với Mentor");
        }

        if (assessmentResultRepository.existsByEvaluatedBy_UserId(user.getUserId())) {
//            if(user.getAssessmentResults() != null && !user.getAssessmentResults().isEmpty()) {}

            throw new IllegalStateException(
                    "Không thể xóa User ID = " + user.getUserId() + " vì đã liên kết với AssessmentResult");
        }

        UserResponse response = userMapper.toResponse(user);

        userRepository.delete(user);

        return response;

    }
}
