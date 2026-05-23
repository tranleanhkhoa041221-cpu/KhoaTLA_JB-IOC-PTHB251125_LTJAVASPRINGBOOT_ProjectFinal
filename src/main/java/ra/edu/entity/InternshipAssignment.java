package ra.edu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "internship_assignments", uniqueConstraints = @UniqueConstraint
        (columnNames = {"student_id", "phase_id"}))
public class InternshipAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long assignmentId;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "mentor_id", nullable = false)
    private Mentor mentor;

    @ManyToOne
    @JoinColumn(name = "phase_id", nullable = false)
    private InternshipPhase phase;

    private LocalDateTime assignedDate = LocalDateTime.now();

    @Enumerated(EnumType.STRING)
    private InternshipAssignmentsStatus status = InternshipAssignmentsStatus.PENDING;

    private LocalDateTime createdAt = null;

    private LocalDateTime updatedAt = null;

    @OneToMany(mappedBy = "assignment")
    private List<AssessmentResult> assessmentResults;


}
