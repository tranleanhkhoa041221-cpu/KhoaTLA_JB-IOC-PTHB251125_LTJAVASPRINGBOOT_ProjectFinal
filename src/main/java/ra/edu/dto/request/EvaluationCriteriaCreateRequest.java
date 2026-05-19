package ra.edu.dto.request;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EvaluationCriteriaCreateRequest {

    @NotBlank(message = "Tên tiêu chí đánh giá không được để trống")
    @Size(max = 200, message = "Tên tiêu chí đánh giá tối đa 200 ký tự")
    private String criterionName;

    @Size(max = 4000, message = "Mô tả chi tiết về tiêu chí tối đa 4000 ký tự")
    private String description;

    @NotNull(message = "Điểm tối đa cho tiêu chí không được để trống")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "maxScore phải lớn hơn 0")
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "maxScore phải nhỏ hơn hoặc bằng 10")
    private BigDecimal maxScore;
}
