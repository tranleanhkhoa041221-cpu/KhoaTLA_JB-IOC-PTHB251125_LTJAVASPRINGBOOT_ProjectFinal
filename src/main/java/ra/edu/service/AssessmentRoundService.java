package ra.edu.service;

import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.dto.response.PaginationResponse;

import java.time.LocalDate;

public interface AssessmentRoundService {

    PaginationResponse<AssessmentRoundResponse> getAllAssessmentRounds(
            int page,
            int size,
            String roundName,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            Long phaseId,
            String phaseName,
            String isActive);

    AssessmentRoundResponse getAssessmentRoundById(Long id);

    AssessmentRoundResponse createAssessmentRound
            (AssessmentRoundCreateRequest request);

    AssessmentRoundResponse updateAssessmentRound
            (Long id, AssessmentRoundUpdateRequest request);

    AssessmentRoundResponse deleteAssessmentRound(Long id);
}
