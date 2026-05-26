package ra.edu.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class AssessmentRoundUpdateRequest {

    @Size(max = 100, message = "Tên đợt đánh giá tối đa 100 ký tự")
    private String roundName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @Size(max = 4000, message = "Mô tả chi tiết tối đa 4000 ký tự")
    private String description;

}
