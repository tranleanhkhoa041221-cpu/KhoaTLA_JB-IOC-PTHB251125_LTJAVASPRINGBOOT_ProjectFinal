package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import ra.edu.entity.InternshipAssignmentsStatus;

@Getter
@Setter
public class InternshipAssignmentUpdateStatusRequest {

    @NotNull(message = "Trạng thái của phân công thực tập không được để trống")
    private InternshipAssignmentsStatus status;
}
