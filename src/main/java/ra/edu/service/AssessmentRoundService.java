package ra.edu.service;

import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundFilterRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.dto.response.PaginationResponse;

public interface AssessmentRoundService {

    PaginationResponse<AssessmentRoundResponse> getAllAssessmentRounds(AssessmentRoundFilterRequest request);

    AssessmentRoundResponse getAssessmentRoundById(Long id);

    AssessmentRoundResponse createAssessmentRound
            (AssessmentRoundCreateRequest request);

    AssessmentRoundResponse updateAssessmentRound
            (Long id, AssessmentRoundUpdateRequest request);

    AssessmentRoundResponse deleteAssessmentRound(Long id);
}
