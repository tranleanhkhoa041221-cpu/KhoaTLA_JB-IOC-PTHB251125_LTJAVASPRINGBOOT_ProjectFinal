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
@Table(name = "internship_phases")
public class InternshipPhase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long phaseId;

    @Column(unique = true, nullable = false, length = 100)
    private String phaseName;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime createdAt = null;

    private LocalDateTime updatedAt = null;

    @OneToMany(mappedBy = "phase")
    private List<InternshipAssignment> internshipAssignments;

    @OneToMany(mappedBy = "phase")
    private List<AssessmentRound> assessmentRounds;

}
