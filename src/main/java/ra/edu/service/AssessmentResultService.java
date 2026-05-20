package ra.edu.service;

import ra.edu.dto.request.AssessmentResultCreateRequest;
import ra.edu.dto.request.AssessmentResultFilterRequest;
import ra.edu.dto.request.AssessmentResultUpdateRequest;
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.dto.response.PaginationResponse;


public interface AssessmentResultService {

    PaginationResponse<AssessmentResultResponse> getAllResult(
            AssessmentResultFilterRequest filter);

    AssessmentResultResponse getResultById(Long id);

    AssessmentResultResponse createResult(
            AssessmentResultCreateRequest request);

    AssessmentResultResponse updateResult(
            Long id, AssessmentResultUpdateRequest request);

    AssessmentResultResponse deleteResult(Long id);
}
