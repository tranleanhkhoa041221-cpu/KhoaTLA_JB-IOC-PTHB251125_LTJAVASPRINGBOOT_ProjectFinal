package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ra.edu.config.jwt.JwtService;
import ra.edu.config.principal.UserPrincipal;
import ra.edu.dto.request.LoginRequest;
import ra.edu.dto.response.LoginResponse;
import ra.edu.dto.response.UserResponse;
import ra.edu.entity.User;
import ra.edu.mapper.UserMapper;
import ra.edu.service.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getInput(),
                        request.getPassword()
                )
        );

        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = principal.getUser();

        String token = jwtService.generateAccessToken(
                user.getUsername(),
                user.getRole().name()
        );

        return new LoginResponse(token, "Bearer");
    }

    @Override
    public UserResponse getCurrentUser(Authentication auth) {
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
        return userMapper.toResponse(principal.getUser());
    }
}
