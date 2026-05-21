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
public class AssessmentResultUpdateRequest {

    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Điểm đạt được cho tiêu chí phải lớn hơn hoặc bằng 0")
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "Điểm đạt được cho tiêu chí phải nhỏ hơn hoặc bằng 10")
    @Digits(
            integer = 2,
            fraction = 2,
            message = "Score chỉ được tối đa 2 số thập phân")
    private BigDecimal score;

    @Size(max = 4000, message = "Nhận xét của giáo viên hướng dẫn cho tiêu chí tối đa 4000 ký tự")
    private String comments;

}
