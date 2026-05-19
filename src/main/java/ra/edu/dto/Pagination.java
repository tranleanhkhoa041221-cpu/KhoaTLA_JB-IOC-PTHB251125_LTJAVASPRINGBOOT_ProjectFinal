package ra.edu.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Pagination {

    private int currentPage;

    private int pageSize;

    private int totalPages;

    private long totalItems;
}
