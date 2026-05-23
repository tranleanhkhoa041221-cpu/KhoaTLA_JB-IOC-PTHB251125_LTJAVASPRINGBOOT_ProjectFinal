package ra.edu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "assessment_rounds", uniqueConstraints = @UniqueConstraint(
        columnNames = {"phase_id", "round_name"}))
public class AssessmentRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roundId;

    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false)
    private InternshipPhase phase;

    @Column(nullable = false, length = 100)
    private String roundName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean isActive = true;

    private LocalDateTime createdAt = null;

    private LocalDateTime updatedAt = null;

    @OneToMany(mappedBy = "round")
    private List<RoundCriteria> roundCriteria;

    @OneToMany(mappedBy = "round")
    private List<AssessmentResult> assessmentResults;
}
