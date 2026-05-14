package ra.edu.service;

import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.request.UserUpdateRoleRequest;
import ra.edu.dto.request.UserUpdateStatusRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.UserResponse;

public interface UserService {
    PaginationResponse<UserResponse> getAllUsers
            (int page,
             int size,
             String username,
             String fullName,
             String email,
             String phoneNumber,
             Boolean isActive,
             String role);
    UserResponse getUserById(Long id);
    UserResponse createUser(UserCreateRequest request);
    UserResponse updateUser(Long id, UserUpdateRequest request);
    UserResponse updateUserStatus(Long id, UserUpdateStatusRequest request);
    UserResponse updateUserRole(Long id, UserUpdateRoleRequest request);
    UserResponse deleteUser(Long id);
}
