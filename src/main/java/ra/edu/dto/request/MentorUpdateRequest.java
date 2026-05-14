package ra.edu.dto.request;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorUpdateRequest {
    @Size(max = 100, message = "Bộ môn/Khoa tối đa 100 ký tự")
    private String department;

    @Size(max = 50, message = "Học hàm/học vị tối đa 50 ký tự")
    private String academicRank;
}
