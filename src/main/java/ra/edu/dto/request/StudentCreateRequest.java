package ra.edu.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
@Getter
@Setter
public class StudentCreateRequest {
    @NotBlank(message = "Mã sinh viên không được để trống")
    @Size(max = 20, message = "Mã sinh viên tối đa 20 ký tự")
    private String studentCode;

    @NotNull(message = "userId không được để trống")
    private Long userId;

    @Size(max = 100, message = "Chuyên ngành tối đa 100 ký tự")
    private String major;

    @Size(max = 50, message = "Tên lớp của sinh viên tối đa 50 ký tự")
    private String className;

    private String address;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;
}
