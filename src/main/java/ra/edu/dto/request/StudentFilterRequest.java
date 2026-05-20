package ra.edu.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class StudentFilterRequest {

    private int page = 1;

    private int size = 10;

    private String studentCode;

    private String address;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    private String username;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String major;

    private String className;


}
