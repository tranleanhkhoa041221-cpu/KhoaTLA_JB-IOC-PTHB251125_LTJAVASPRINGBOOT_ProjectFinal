package ra.edu.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Thông tin đăng nhập không được để trống")
    private String input;

    @NotBlank(message = "Password không được để trống")
    private String password;
}
