package ra.edu.service;

import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.RoundCriteriaResponse;

import java.math.BigDecimal;

public interface RoundCriteriaService {

    PaginationResponse<RoundCriteriaResponse> getAllRoundCriteria(
            int page,
            int size,
            Long roundId,
            Long criterionId,
            String roundName,
            String criterionName,
            BigDecimal weight,
            BigDecimal minWeight,
            BigDecimal maxWeight);

    RoundCriteriaResponse getRoundCriteriaById(Long id);

    RoundCriteriaResponse createRoundCriteria
            (RoundCriteriaCreateRequest request);

    RoundCriteriaResponse updateRoundCriteria
            (Long id, RoundCriteriaUpdateRequest request);

    RoundCriteriaResponse deleteRoundCriteria(Long id);
}
