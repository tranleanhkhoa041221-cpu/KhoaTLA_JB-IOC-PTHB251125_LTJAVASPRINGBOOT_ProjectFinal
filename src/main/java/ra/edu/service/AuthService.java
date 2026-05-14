package ra.edu.service;

import org.springframework.security.core.Authentication;
import ra.edu.dto.request.LoginRequest;
import ra.edu.dto.response.LoginResponse;
import ra.edu.dto.response.UserResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

    UserResponse getCurrentUser(Authentication auth);
}
