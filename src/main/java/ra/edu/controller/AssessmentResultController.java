package ra.edu.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssessmentResultCreateRequest;
import ra.edu.dto.request.AssessmentResultUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.entity.InternshipAssignmentsStatus;
import ra.edu.service.AssessmentResultService;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/assessment-results")
@RequiredArgsConstructor
@Validated
public class AssessmentResultController {

    private final AssessmentResultService assessmentResultService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long assignmentId,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long mentorId,
            @RequestParam(required = false) Long phaseId,
            @RequestParam(required = false) InternshipAssignmentsStatus assignmentStatus,
            @RequestParam(required = false) Long roundId,
            @RequestParam(required = false) Long criterionId,
            @RequestParam(required = false) Long evaluatedById,
            @RequestParam(required = false) String studentUsername,
            @RequestParam(required = false) String studentFullName,
            @RequestParam(required = false) String studentEmail,
            @RequestParam(required = false) String studentPhoneNumber,
            @RequestParam(required = false) String mentorUsername,
            @RequestParam(required = false) String mentorFullName,
            @RequestParam(required = false) String mentorEmail,
            @RequestParam(required = false) String mentorPhoneNumber,
            @RequestParam(required = false) String phaseName,
            @RequestParam(required = false) String roundName,
            @RequestParam(required = false) String criterionName,
            @RequestParam(required = false) String evaluatedByUsername,
            @RequestParam(required = false)
            String evaluatedByFullName,
            @RequestParam(required = false)
            String evaluatedByEmail,
            @RequestParam(required = false)
            String evaluatedByPhoneNumber,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = true,
                    message = "score phải lớn hơn hoặc bằng 0")
            @DecimalMax(
                    value = "10.0",
                    inclusive = true,
                    message = "score phải nhỏ hơn hoặc bằng 10"
            )
            BigDecimal score,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = true,
                    message = "minScore phải lớn hơn hoặc bằng 0")
            @DecimalMax(
                    value = "10.0",
                    inclusive = true,
                    message = "minScore phải nhỏ hơn hoặc bằng 10"
            )
            BigDecimal minScore,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = true,
                    message = "maxScore phải lớn hơn hoặc bằng 0")
            @DecimalMax(
                    value = "10.0",
                    inclusive = true,
                    message = "maxScore phải nhỏ hơn hoặc bằng 10"
            )
            BigDecimal maxScore,
            @RequestParam(required = false) String comments,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime evaluationDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime minEvaluationDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime maxEvaluationDate) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách AssessmentResult thành công",
                        assessmentResultService.getAllResults(
                                page,
                                size,
                                assignmentId,
                                studentId,
                                mentorId,
                                phaseId,
                                assignmentStatus,
                                roundId,
                                criterionId,
                                evaluatedById,
                                studentUsername,
                                studentFullName,
                                studentEmail,
                                studentPhoneNumber,
                                mentorUsername,
                                mentorFullName,
                                mentorEmail,
                                mentorPhoneNumber,
                                phaseName,
                                roundName,
                                criterionName,
                                evaluatedByUsername,
                                evaluatedByFullName,
                                evaluatedByEmail,
                                evaluatedByPhoneNumber,
                                score,
                                minScore,
                                maxScore,
                                comments,
                                evaluationDate,
                                minEvaluationDate,
                                maxEvaluationDate)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết AssessmentResult thành công",
                        assessmentResultService.getResultById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody AssessmentResultCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "Tạo AssessmentResult thành công",
                                assessmentResultService.createResult(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentResultUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật AssessmentResult thành công",
                        assessmentResultService.updateResult(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Xóa AssessmentResult thành công",
                        assessmentResultService.deleteResult(id)));
    }


}
