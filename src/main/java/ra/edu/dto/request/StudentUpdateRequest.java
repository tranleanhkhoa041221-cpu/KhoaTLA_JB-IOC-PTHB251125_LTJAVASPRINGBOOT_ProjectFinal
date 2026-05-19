package ra.edu.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentUpdateRequest {

    @Size(max = 100, message = "Chuyên ngành tối đa 100 ký tự")
    private String major;

    @Size(max = 50, message = "Tên lớp của sinh viên tối đa 50 ký tự")
    private String className;

    @Size(max = 255, message = "Địa chỉ hiện tại của sinh viên tối đa 255 ký tự")
    private String address;

    @Past(message = "Ngày sinh không được ở tương lai")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
}
