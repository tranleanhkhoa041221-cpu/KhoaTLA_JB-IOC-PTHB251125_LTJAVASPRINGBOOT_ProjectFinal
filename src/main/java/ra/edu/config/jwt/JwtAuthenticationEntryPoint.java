package ra.edu.config.jwt;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Throwable cause = (Throwable) request.getAttribute("jwt_exception");

        String message = "Unauthorized: Authentication required";

        if (cause instanceof ExpiredJwtException) {
            message = "Expired token";
        } else if (cause instanceof MalformedJwtException) {
            message = "Invalid token";
        } else if (cause instanceof UnsupportedJwtException) {
            message = "Unsupported token";
        } else if (cause instanceof IllegalArgumentException) {
            message = "Jwt key string invalid";
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> body = new HashMap<>();
        body.put("success", false);
        body.put("message", message);
        body.put("data", null);
        body.put("errors", null);
        body.put("timestamp", LocalDateTime.now().format(
                DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")
        ));

        new ObjectMapper().writeValue(response.getOutputStream(), body);

    }
}
