package ra.edu.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorFilterRequest {

    private int page = 1;

    private int size = 10;

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String department;

    private String academicRank;
}
