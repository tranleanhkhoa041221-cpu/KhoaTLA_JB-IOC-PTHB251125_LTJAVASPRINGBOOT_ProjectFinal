package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RoundCriteriaCreateRequest {

    @NotNull(message = "RoundId không được để trống")
    private Long roundId;

    @NotNull(message = "CriterionId không được để trống")
    private Long criterionId;

    @NotNull(message = "Trọng số không được để trống")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Weight phải lớn hơn 0")
    @DecimalMax(
            value = "1.0",
            inclusive = true,
            message = "Weight phải nhỏ hơn hoặc bằng 1"
    )
    @Digits(
            integer = 1,
            fraction = 2,
            message = "Weight chỉ được tối đa 2 số thập phân")

    private BigDecimal weight;
}
