package ra.edu.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RoundCriteriaResponse {

    private Long roundCriterionId;

    private Long roundId;
    private String roundName;

    private Long criterionId;
    private String criterionName;

    private BigDecimal weight;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime updatedAt;
}
