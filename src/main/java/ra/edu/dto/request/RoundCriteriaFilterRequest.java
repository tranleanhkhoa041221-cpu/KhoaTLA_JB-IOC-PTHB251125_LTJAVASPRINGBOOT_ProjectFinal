package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoundCriteriaFilterRequest {

    private int page = 1;

    private int size = 10;

    private Long roundId;

    private Long criterionId;

    private String roundName;

    private String criterionName;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Weight phải lớn hơn 0"
    )
    @DecimalMax(
            value = "1.0",
            inclusive = true,
            message = "Weight phải nhỏ hơn hoặc bằng 1"
    )
    private BigDecimal weight;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "minWeight phải lớn hơn 0"
    )
    @DecimalMax(
            value = "1.0",
            inclusive = true,
            message = "minWeight phải nhỏ hơn hoặc bằng 1"
    )
    private BigDecimal minWeight;

    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "maxWeight phải lớn hơn 0"
    )
    @DecimalMax(
            value = "1.0",
            inclusive = true,
            message = "maxWeight phải nhỏ hơn hoặc bằng 1"
    )
    private BigDecimal maxWeight;
}
