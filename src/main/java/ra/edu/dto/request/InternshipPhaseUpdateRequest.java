package ra.edu.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class InternshipPhaseUpdateRequest {

    @Size(max = 100, message = "Tên giai đoạn tối đa 100 ký tự")
    private String phaseName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @Size(max = 4000, message = "Mô tả chi tiết về giai đoạn tối đa 4000 ký tự")
    private String description;
}
