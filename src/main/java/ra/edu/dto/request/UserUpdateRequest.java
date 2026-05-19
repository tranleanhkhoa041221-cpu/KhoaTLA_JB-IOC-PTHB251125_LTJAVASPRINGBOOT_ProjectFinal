package ra.edu.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {

    @Size(max = 50, message = "Tên người dùng tối đa 50 ký tự")
    private String username;

    @Size(max = 100, message = "Họ và tên đầy đủ của người dùng tối đa 100 ký tự")
    private String fullName;

    @Email(message = "Email không hợp lệ. Ví dụ hợp lệ: example@gmail.com")
    @Size(max = 50, message = "Email tối đa 50 ký tự")
    private String email;

    @Pattern(regexp = "^0([35789])[0-9]{8}$", message = "Số điện thoại không hợp lệ. Phải gồm 10 số và bắt đầu bằng 03, 05, 07, 08 hoặc 09")
    @Size(max = 20, message = "phoneNumber tối đa 20 ký tự")
    private String phoneNumber;
}
