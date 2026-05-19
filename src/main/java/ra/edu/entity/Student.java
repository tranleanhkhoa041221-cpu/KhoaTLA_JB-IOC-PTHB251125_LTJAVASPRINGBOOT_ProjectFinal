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
@Table(name = "students")
public class Student {

    @Id
    private Long studentId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "student_id")
    private User user;

    @Column(unique = true, nullable = false, length = 20)
    private String studentCode;

    @Column(length = 100)
    private String major;

    @Column(length = 50)
    private String className;

    private LocalDate dateOfBirth;

    private String address;

    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "student")
    private List<InternshipAssignment> internshipAssignments;
}
