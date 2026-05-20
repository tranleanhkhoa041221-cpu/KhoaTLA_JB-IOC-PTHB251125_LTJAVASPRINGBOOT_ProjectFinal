package ra.edu.dto.request;

import lombok.Getter;
import lombok.Setter;
import ra.edu.entity.UserRole;

@Getter
@Setter
public class UserFilterRequest {

    private int page = 1;

    private int size = 10;

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String isActive;

    private UserRole role;
}
