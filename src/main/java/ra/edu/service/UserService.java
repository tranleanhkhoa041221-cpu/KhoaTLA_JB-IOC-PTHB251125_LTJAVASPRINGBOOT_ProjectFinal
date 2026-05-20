package ra.edu.service;

import ra.edu.dto.request.*;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.UserResponse;

public interface UserService {

    PaginationResponse<UserResponse> getAllUsers(UserFilterRequest filter);

    UserResponse getUserById(Long id);

    UserResponse createUser(UserCreateRequest request);

    UserResponse updateUser(Long id, UserUpdateRequest request);

    UserResponse updateUserStatus(Long id, UserUpdateStatusRequest request);

    UserResponse updateUserRole(Long id, UserUpdateRoleRequest request);

    UserResponse deleteUser(Long id);
}
