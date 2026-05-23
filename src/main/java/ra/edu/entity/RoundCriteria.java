package ra.edu.entity;

import jakarta.persistence.*;
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
@Entity
@Table(name = "round_criteria", uniqueConstraints = @UniqueConstraint
        (columnNames = {"round_id", "criterion_id"}))
public class RoundCriteria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundCriterionId;

    @ManyToOne
    @JoinColumn(name = "round_id", nullable = false)
    private AssessmentRound round;

    @ManyToOne
    @JoinColumn(name = "criterion_id", nullable = false)
    private EvaluationCriteria criterion;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal weight;

    private LocalDateTime createdAt = null;

    private LocalDateTime updatedAt = null;

}
