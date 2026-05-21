package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EvaluationCriteriaUpdateRequest {

    @Size(max = 200, message = "Tên tiêu chí đánh giá tối đa 200 ký tự")
    private String criterionName;

    @Size(max = 4000, message = "Mô tả chi tiết về tiêu chí tối đa 4000 ký tự")
    private String description;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Điểm tối đa cho tiêu chí phải lớn hơn 0")
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "Điểm tối đa cho tiêu chí phải nhỏ hơn hoặc bằng 10")
    @Digits(
            integer = 2,
            fraction = 2,
            message = "maxScore chỉ được tối đa 2 số thập phân")
    private BigDecimal maxScore;

}
