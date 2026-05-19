package ra.edu.service;

import ra.edu.dto.request.EvaluationCriteriaCreateRequest;
import ra.edu.dto.request.EvaluationCriteriaUpdateRequest;
import ra.edu.dto.response.EvaluationCriteriaResponse;
import ra.edu.dto.response.PaginationResponse;

import java.math.BigDecimal;

public interface EvaluationCriteriaService {

    PaginationResponse<EvaluationCriteriaResponse> getAllCriteria(
            int page,
            int size,
            String criterionName,
            String description,
            BigDecimal maxScore,
            BigDecimal minMaxScore,
            BigDecimal maxMaxScore);

    EvaluationCriteriaResponse getCriterionById(Long id);

    EvaluationCriteriaResponse createCriterion
            (EvaluationCriteriaCreateRequest request);

    EvaluationCriteriaResponse updateCriterion
            (Long id, EvaluationCriteriaUpdateRequest request);

    EvaluationCriteriaResponse deleteCriterion(Long id);
}
