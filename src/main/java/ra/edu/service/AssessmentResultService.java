package ra.edu.service;

import ra.edu.dto.request.AssessmentResultCreateRequest;
import ra.edu.dto.request.AssessmentResultUpdateRequest;
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AssessmentResultService {

    PaginationResponse<AssessmentResultResponse> getAllResults(
            int page,
            int size,
            Long assignmentId,
            Long studentId,
            Long mentorId,
            Long phaseId,
            InternshipAssignmentsStatus assignmentStatus,
            Long roundId,
            Long criterionId,
            Long evaluatedById,
            String studentUsername,
            String studentFullName,
            String studentEmail,
            String studentPhoneNumber,
            String mentorUsername,
            String mentorFullName,
            String mentorEmail,
            String mentorPhoneNumber,
            String phaseName,
            String roundName,
            String criterionName,
            String evaluatedByUsername,
            String evaluatedByFullName,
            String evaluatedByEmail,
            String evaluatedByPhoneNumber,
            BigDecimal score,
            BigDecimal minScore,
            BigDecimal maxScore,
            String comments,
            LocalDateTime evaluationDate,
            LocalDateTime minEvaluationDate,
            LocalDateTime maxEvaluationDate);

    AssessmentResultResponse getResultById(Long id);

    AssessmentResultResponse createResult(
            AssessmentResultCreateRequest request);

    AssessmentResultResponse updateResult(
            Long id, AssessmentResultUpdateRequest request);

    AssessmentResultResponse deleteResult(Long id);
}
