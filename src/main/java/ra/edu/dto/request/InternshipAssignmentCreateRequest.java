package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InternshipAssignmentCreateRequest {

    @NotNull(message = "StudentId không được để trống")
    private Long studentId;

    @NotNull(message = "MentorId không được để trống")
    private Long mentorId;

    @NotNull(message = "PhaseId không được để trống")
    private Long phaseId;

}
