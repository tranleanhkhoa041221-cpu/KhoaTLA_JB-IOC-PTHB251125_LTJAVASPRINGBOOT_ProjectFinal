package ra.edu.service;

import ra.edu.dto.request.EvaluationCriteriaCreateRequest;
import ra.edu.dto.request.EvaluationCriteriaFilterRequest;
import ra.edu.dto.request.EvaluationCriteriaUpdateRequest;
import ra.edu.dto.response.EvaluationCriteriaResponse;
import ra.edu.dto.response.PaginationResponse;

public interface EvaluationCriteriaService {

    PaginationResponse<EvaluationCriteriaResponse> getAllCriteria(
            EvaluationCriteriaFilterRequest request);

    EvaluationCriteriaResponse getCriterionById(Long id);

    EvaluationCriteriaResponse createCriterion
            (EvaluationCriteriaCreateRequest request);

    EvaluationCriteriaResponse updateCriterion
            (Long id, EvaluationCriteriaUpdateRequest request);

    void deleteCriterion(Long id);
}
