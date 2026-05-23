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
@Table(name = "mentors")
public class Mentor {

    @Id
    private Long mentorId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "mentor_id")
    private User user;

    @Column(length = 100)
    private String department;

    @Column(length = 50)
    private String academicRank;

    private LocalDateTime createdAt = null;

    private LocalDateTime updatedAt = null;

    @OneToMany(mappedBy = "mentor")
    private List<InternshipAssignment> internshipAssignments;
}
