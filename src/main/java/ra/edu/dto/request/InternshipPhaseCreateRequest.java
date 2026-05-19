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
public class InternshipPhaseCreateRequest {

    @NotBlank(message = "Tên giai đoạn thực tập không được để trống")
    @Size(max = 100, message = "Tên giai đoạn thực tập tối đa 100 ký tự")
    private String phaseName;

    @NotNull(message = "Ngày bắt đầu giai đoạn thực tập không được để trống")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc giai đoạn thực tập không được để trống")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @Size(max = 4000, message = "Mô tả chi tiết về giai đoạn tối đa 4000 ký tự")
    private String description;
}
