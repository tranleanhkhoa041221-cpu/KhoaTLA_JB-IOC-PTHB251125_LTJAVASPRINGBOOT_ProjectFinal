package ra.edu.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class InternshipAssignmentResponse {

    private Long assignmentId;

    private Long studentId;
    private String studentUsername;
    private String studentFullName;
    private String studentEmail;
    private String studentPhoneNumber;

    private Long mentorId;
    private String mentorUsername;
    private String mentorFullName;
    private String mentorEmail;
    private String mentorPhoneNumber;

    private Long phaseId;
    private String phaseName;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime assignedDate;

    private InternshipAssignmentsStatus status;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime updatedAt;
}
