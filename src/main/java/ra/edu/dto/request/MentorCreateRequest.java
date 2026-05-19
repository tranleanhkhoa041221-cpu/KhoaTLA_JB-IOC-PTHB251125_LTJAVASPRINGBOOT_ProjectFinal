package ra.edu.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MentorCreateRequest {

    @NotNull(message = "userId không được để trống")
    private Long userId;

    @Size(max = 100, message = "Bộ môn/Khoa tối đa 100 ký tự")
    private String department;

    @Size(max = 50, message = "Học hàm/học vị tối đa 50 ký tự")
    private String academicRank;
}
