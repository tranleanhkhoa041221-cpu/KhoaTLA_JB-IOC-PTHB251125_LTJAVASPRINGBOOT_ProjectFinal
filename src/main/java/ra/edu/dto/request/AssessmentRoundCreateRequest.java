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
public class AssessmentRoundCreateRequest {

    @NotNull(message = "phaseID không được để trống")
    private Long phaseId;

    @NotBlank(message = "Tên đợt đánh giá không được để trống")
    @Size(max = 100, message = "Tên đợt đánh giá tối đa 100 ký tự")
    private String roundName;

    @NotNull(message = "Ngày bắt đầu đợt đánh giá không được để trống")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc đợt đánh giá không được để trống")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    @Size(max = 4000, message = "Mô tả về đợt đánh giá tối đa 4000 ký tự")
    private String description;

}
