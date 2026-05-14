package ra.edu.dto.response;

import lombok.*;
import ra.edu.dto.Pagination;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class PaginationResponse<T> {

    private List<T> items;

    private Pagination pagination;
}
