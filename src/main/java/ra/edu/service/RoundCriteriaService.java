package ra.edu.service;

import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaFilterRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.RoundCriteriaResponse;

public interface RoundCriteriaService {

    PaginationResponse<RoundCriteriaResponse> getAllRoundCriteria(
            RoundCriteriaFilterRequest request);

    RoundCriteriaResponse getRoundCriteriaById(Long id);

    RoundCriteriaResponse createRoundCriteria
            (RoundCriteriaCreateRequest request);

    RoundCriteriaResponse updateRoundCriteria
            (Long id, RoundCriteriaUpdateRequest request);

    RoundCriteriaResponse deleteRoundCriteria(Long id);
}
