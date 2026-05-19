package ra.edu.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateStatusRequest {

    @NotBlank(message = "isActive không được để trống")
    @Pattern(
            regexp = "true|false",
            message = "isActive không hợp lệ. Chỉ được true hoặc false"
    )
    private String isActive;

//    @NotNull(message = "isActive không được để trống")
//    private Boolean isActive;
}
