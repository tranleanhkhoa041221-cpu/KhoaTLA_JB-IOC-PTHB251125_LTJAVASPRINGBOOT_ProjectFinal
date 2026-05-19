package ra.edu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import ra.edu.entity.UserRole;

@Getter
@Setter
public class UserUpdateRoleRequest {

//    @NotBlank(message = "role không được để trống")
//    @Pattern(
//            regexp = "ADMIN|MENTOR|STUDENT",
//            message = "Role không hợp lệ. Giá trị hợp lệ: ADMIN, MENTOR, STUDENT"
//    )
//    private String role;

    @NotNull(message = "role không được để trống")
    private UserRole role;
}
