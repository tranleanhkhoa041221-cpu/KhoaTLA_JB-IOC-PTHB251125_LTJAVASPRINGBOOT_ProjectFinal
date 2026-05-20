package ra.edu.dto.request;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class InternshipAssignmentFilterRequest {

    private int page = 1;

    private int size = 10;

    private Long studentId;

    private Long mentorId;

    private Long phaseId;

    private String studentUsername;

    private String studentFullName;

    private String studentEmail;

    private String studentPhoneNumber;

    private String mentorUsername;

    private String mentorFullName;

    private String mentorEmail;

    private String mentorPhoneNumber;

    private InternshipAssignmentsStatus status;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime assignedDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime minAssignedDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime maxAssignedDate;




}
