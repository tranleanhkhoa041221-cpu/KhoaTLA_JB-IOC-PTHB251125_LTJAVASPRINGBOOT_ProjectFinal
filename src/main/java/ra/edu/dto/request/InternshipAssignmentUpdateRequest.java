package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class InternshipAssignmentUpdateRequest {

    @NotNull(message = "MentorId không được để trống")
    private Long mentorId;

}
