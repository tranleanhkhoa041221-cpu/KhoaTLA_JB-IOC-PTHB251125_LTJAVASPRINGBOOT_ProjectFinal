package ra.edu.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import ra.edu.entity.UserRole;

@Getter
@Setter
public class UserCreateRequest {

    @NotBlank(message = "Username không được để trống")
    @Size(max = 50, message = "Tên người dùng tối đa 50 ký tự")
    private String username;

    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, message = "Password phải có ít nhất 6 ký tự")
    private String password;

    @NotBlank(message = "Full name không được để trống")
    @Size(max = 100, message = "Họ và tên đầy đủ của người dùng tối đa 100 ký tự")
    private String fullName;

    @Email(message = "Email không hợp lệ. Ví dụ hợp lệ: example@gmail.com")
    @NotBlank(message = "Email không được để trống")
    @Size(max = 50, message = "Email tối đa 50 ký tự")
    private String email;

    @Pattern(regexp = "^0([35789])[0-9]{8}$", message = "Số điện thoại không hợp lệ. Phải gồm 10 số và bắt đầu bằng 03, 05, 07, 08 hoặc 09")
    @Size(max = 20, message = "phoneNumber tối đa 20 ký tự")
    private String phoneNumber;

//    @NotBlank(message = "role không được để trống")
//    @Pattern(
//            regexp = "ADMIN|MENTOR|STUDENT",
//            message = "Role không hợp lệ. Giá trị hợp lệ: ADMIN, MENTOR, STUDENT"
//    )
//    private String role;

    @NotNull(message = "Role không được để trống")
    private UserRole role;
}
