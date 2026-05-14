package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.LoginRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(
                ApiResponse.success("Login successfully", authService.login(request))
        );
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<?>> me(Authentication auth) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Get current user successfully",
                        authService.getCurrentUser(auth)
                )
        );
    }
}
