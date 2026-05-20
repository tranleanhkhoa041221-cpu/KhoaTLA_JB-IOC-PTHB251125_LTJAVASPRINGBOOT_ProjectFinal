package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class EvaluationCriteriaFilterRequest {

    private int page = 1;

    private int size = 10;

    private String criterionName;

    private String description;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "maxScore phải lớn hơn 0"
    )
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "maxScore phải nhỏ hơn hoặc bằng 10"
    )
    private BigDecimal maxScore;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "minMaxScore phải lớn hơn 0"
    )
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "minMaxScore phải nhỏ hơn hoặc bằng 10"
    )
    private BigDecimal minMaxScore;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "maxMaxScore phải lớn hơn 0"
    )
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "maxMaxScore phải nhỏ hơn hoặc bằng 10"
    )
    private BigDecimal maxMaxScore;
}
