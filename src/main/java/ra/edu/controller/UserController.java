package ra.edu.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.request.UserUpdateRoleRequest;
import ra.edu.dto.request.UserUpdateStatusRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.entity.UserRole;
import ra.edu.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String isActive,
            @RequestParam(required = false) UserRole role) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách người dùng thành công",
                        userService.getAllUsers(page, size, username, fullName, email, phoneNumber, isActive, role)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết người dùng thành công",
                        userService.getUserById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody UserCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo người dùng thành công",
                        userService.createUser(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật người dùng thành công",
                        userService.updateUser(id, request)));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateStatusRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật trạng thái người dùng thành công",
                        userService.updateUserStatus(id, request)));
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<ApiResponse<?>> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRoleRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật vai trò người dùng thành công",
                        userService.updateUserRole(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Xóa người dùng thành công", userService.deleteUser(id)));
    }
}
