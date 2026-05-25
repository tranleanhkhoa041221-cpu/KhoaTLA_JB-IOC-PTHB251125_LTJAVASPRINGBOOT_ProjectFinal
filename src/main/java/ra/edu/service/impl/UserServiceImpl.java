package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.*;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.UserResponse;
import ra.edu.entity.User;
import ra.edu.entity.UserRole;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.NotFoundException;
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
    public PaginationResponse<UserResponse> getAllUsers(
            UserFilterRequest filter) {

        Boolean active = null;

        if (filter.getIsActive() != null
                && !filter.getIsActive().isBlank()) {

            if (!filter.getIsActive().equalsIgnoreCase("true")
                    && !filter.getIsActive().equalsIgnoreCase("false")) {

                throw new BadRequestException(
                        "isActive không hợp lệ. Chỉ được true hoặc false");
            }

            active = Boolean.parseBoolean(filter.getIsActive());
        }

        Pageable pageable = PageRequest.of(
                filter.getPage() - 1,
                filter.getSize(),
                Sort.by("userId").ascending());

        Page<User> userPage;

        if (filter.getUsername() != null
                && !filter.getUsername().isBlank()) {

            userPage =
                    userRepository
                            .findAllByUsernameContainingIgnoreCase(
                                    filter.getUsername(),
                                    pageable);

        } else if (filter.getFullName() != null
                && !filter.getFullName().isBlank()) {

            userPage =
                    userRepository
                            .findAllByFullNameContainingIgnoreCase(
                                    filter.getFullName(),
                                    pageable);

        } else if (filter.getEmail() != null
                && !filter.getEmail().isBlank()) {

            userPage =
                    userRepository
                            .findAllByEmailContainingIgnoreCase(
                                    filter.getEmail(),
                                    pageable);

        } else if (filter.getPhoneNumber() != null
                && !filter.getPhoneNumber().isBlank()) {

            userPage =
                    userRepository
                            .findAllByPhoneNumberContainingIgnoreCase(
                                    filter.getPhoneNumber(),
                                    pageable);

        } else if (active != null) {

            userPage =
                    userRepository
                            .findAllByIsActive(
                                    active,
                                    pageable);

        } else if (filter.getRole() != null) {

            userPage =
                    userRepository
                            .findAllByRole(
                                    filter.getRole(),
                                    pageable);

        } else {

            userPage = userRepository.findAll(pageable);
        }

        List<UserResponse> items =
                userPage.getContent()
                        .stream()
                        .map(userMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(filter.getPage())
                .pageSize(filter.getSize())
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

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + id));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse createUser(
            UserCreateRequest request) {

        if (userRepository.existsByUsernameIgnoreCase(
                request.getUsername())) {

            throw new ConflictException(
                    "Username đã tồn tại");
        }

        if (userRepository.existsByEmailIgnoreCase(
                request.getEmail())) {

            throw new ConflictException(
                    "Email đã tồn tại");
        }

        if (userRepository.existsByPhoneNumber(
                request.getPhoneNumber())) {

            throw new ConflictException(
                    "Số điện thoại đã tồn tại");
        }

        User user = userMapper.toEntity(request);

        user.setPasswordHash(
                passwordEncoder.encode(request.getPassword()));

        user.setIsActive(true);

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }


    @Override
    public UserResponse updateUser(
            Long id,
            UserUpdateRequest request) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + id));

        if (request.getUsername() != null) {

            if (request.getUsername().isBlank()) {

                throw new BadRequestException(
                        "username không được để trống");
            }


            if (!user.getUsername()
                    .equalsIgnoreCase(request.getUsername())
                    && userRepository.existsByUsernameIgnoreCase(
                    request.getUsername())) {

                throw new ConflictException(
                        "Username đã tồn tại");
            }
        }

        if (request.getFullName() != null) {

            request.setFullName(
                    request.getFullName().trim());

            if (request.getFullName().isBlank()) {

                throw new BadRequestException(
                        "Họ và tên không được để trống");
            }
        }

        if (request.getEmail() != null) {

            request.setEmail(
                    request.getEmail().trim());

            if (request.getEmail().isBlank()) {

                throw new BadRequestException(
                        "Email không được để trống");
            }


            if (!user.getEmail()
                    .equalsIgnoreCase(request.getEmail())
                    && userRepository.existsByEmailIgnoreCase(
                    request.getEmail())) {

                throw new ConflictException(
                        "Email đã tồn tại");
            }
        }

        if (request.getPhoneNumber() != null) {

            request.setPhoneNumber(
                    request.getPhoneNumber().trim());

            if (request.getPhoneNumber().isBlank()) {

                throw new BadRequestException(
                        "Số điện thoại không được để trống");
            }

            if (!user.getPhoneNumber()
                    .equalsIgnoreCase(request.getPhoneNumber())
                    && userRepository.existsByPhoneNumber(
                    request.getPhoneNumber())) {

                throw new ConflictException(
                        "Số điện thoại đã tồn tại");
            }
        }

        userMapper.updateEntityFromDto(request, user);

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse updateUserStatus(
            Long id,
            UserUpdateStatusRequest request) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + id));

        if (user.getRole() == UserRole.ADMIN) {

            throw new ConflictException(
                    "Không được phép thay đổi trạng thái của ADMIN");
        }


        boolean hasStudent =
                studentRepository.existsByUser_UserId(user.getUserId());

        boolean hasMentor =
                mentorRepository.existsByUser_UserId(user.getUserId());

        boolean hasAssessmentResult =
                assessmentResultRepository
                        .existsByEvaluatedBy_UserId(user.getUserId());

        if (hasMentor && hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể thay đổi trạng thái User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Mentor và AssessmentResult");
        }

        if (hasStudent) {

            throw new ConflictException(
                    "Không thể thay đổi trạng thái User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Student");
        }

        if (hasMentor) {

            throw new ConflictException(
                    "Không thể thay đổi trạng thái User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Mentor");
        }

        if (hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể thay đổi trạng thái User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với AssessmentResult");
        }

        boolean oldStatus = user.getIsActive();
        boolean newStatus = Boolean.parseBoolean(request.getIsActive());

        if (oldStatus == newStatus) {
            throw new BadRequestException(
                    "Tài khoản này đã ở trạng thái " + (oldStatus ? "ĐÃ KÍCH HOẠT" : "BỊ KHÓA") + " rồi!");
        }

        user.setIsActive(newStatus);

//        user.setIsActive(request.getIsActive());

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }


    @Override
    public UserResponse updateUserRole(
            Long id,
            UserUpdateRoleRequest request) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + id));

        if (user.getRole() == UserRole.ADMIN) {

            throw new ConflictException(
                    "Không được phép thay đổi role của ADMIN");
        }

        boolean hasStudent =
                studentRepository.existsByUser_UserId(user.getUserId());

        boolean hasMentor =
                mentorRepository.existsByUser_UserId(user.getUserId());

        boolean hasAssessmentResult =
                assessmentResultRepository
                        .existsByEvaluatedBy_UserId(user.getUserId());

        if (hasMentor && hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể thay đổi role của User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Mentor và AssessmentResult");
        }

        if (hasStudent) {

            throw new ConflictException(
                    "Không thể thay đổi role của User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Student");
        }

        if (hasMentor) {

            throw new ConflictException(
                    "Không thể thay đổi role của User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Mentor");
        }

        if (hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể thay đổi role của User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với AssessmentResult");
        }

        UserRole oldRole = user.getRole();
        UserRole newRole = request.getRole();

        if (oldRole == newRole) {
            throw new BadRequestException(
                    "Tài khoản này đã có quyền (role) là " + oldRole + " rồi!");
        }

        user.setRole(newRole);

//        user.setRole(UserRole.valueOf(request.getRole()));

        user.setUpdatedAt(LocalDateTime.now());

        userRepository.save(user);

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse deleteUser(Long id) {

        User user =
                userRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy User với ID = "
                                                + id));

        if (user.getRole() == UserRole.ADMIN) {

            throw new ConflictException(
                    "Không được phép xóa tài khoản ADMIN");
        }

        boolean hasStudent =
                studentRepository.existsByUser_UserId(user.getUserId());

        boolean hasMentor =
                mentorRepository.existsByUser_UserId(user.getUserId());

        boolean hasAssessmentResult =
                assessmentResultRepository
                        .existsByEvaluatedBy_UserId(user.getUserId());

        if (hasMentor && hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể xóa User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Mentor và AssessmentResult");
        }

        if (hasStudent) {

            throw new ConflictException(
                    "Không thể xóa User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Student");
        }

        if (hasMentor) {

            throw new ConflictException(
                    "Không thể xóa User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với Mentor");
        }

        if (hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể xóa User ID = "
                            + user.getUserId()
                            + " vì đã liên kết với AssessmentResult");
        }

        UserResponse response = userMapper.toResponse(user);

        userRepository.delete(user);

        return response;
    }
}
