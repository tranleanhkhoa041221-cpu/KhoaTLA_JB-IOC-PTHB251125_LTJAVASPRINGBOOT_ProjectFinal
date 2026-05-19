package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AssessmentResultCreateRequest {

    @NotNull(message = "Phân công thực tập không được để trống")
    private Long assignmentId;

    @NotNull(message = "Đợt đánh giá không được để trống")
    private Long roundId;

    @NotNull(message = "Tiêu chí đánh giá không được để trống")
    private Long criterionId;

    @NotNull(message = "Điểm không được để trống")
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "score phải lớn hơn hoặc bằng 0")
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "score phải nhỏ hơn hoặc bằng 10"
    )
    private BigDecimal score;

    @Size(max = 4000, message = "Nhận xét của giáo viên hướng dẫn cho tiêu chí tối đa 4000 ký tự")
    private String comments;


}
