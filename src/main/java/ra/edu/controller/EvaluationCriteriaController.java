package ra.edu.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.EvaluationCriteriaCreateRequest;
import ra.edu.dto.request.EvaluationCriteriaUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.EvaluationCriteriaService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/evaluation-criteria")
@RequiredArgsConstructor
@Validated
public class EvaluationCriteriaController {

    private final EvaluationCriteriaService evaluationCriteriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String criterionName,
            @RequestParam(required = false) String description,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = false,
                    message = "maxScore phải lớn hơn 0")
            @DecimalMax(
                    value = "10.0",
                    inclusive = true,
                    message = "maxScore phải nhỏ hơn hoặc bằng 10")
            BigDecimal maxScore,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = false,
                    message = "minMaxScore phải lớn hơn 0")
            @DecimalMax(
                    value = "10.0",
                    inclusive = true,
                    message = "minMaxScore phải nhỏ hơn hoặc bằng 10")
            BigDecimal minMaxScore,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = false,
                    message = "maxMaxScore phải lớn hơn 0")
            @DecimalMax(
                    value = "10.0",
                    inclusive = true,
                    message = "maxMaxScore phải nhỏ hơn hoặc bằng 10")
            BigDecimal maxMaxScore) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách tiêu chí đánh giá thành công",
                        evaluationCriteriaService.getAllCriteria(page, size, criterionName, description, maxScore, minMaxScore, maxMaxScore)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết tiêu chí đánh giá thành công",
                        evaluationCriteriaService.getCriterionById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody EvaluationCriteriaCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo tiêu chí đánh giá thành công",
                        evaluationCriteriaService.createCriterion(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationCriteriaUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật tiêu chí đánh giá thành công",
                        evaluationCriteriaService.updateCriterion(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Xóa tiêu chí đánh giá thành công",
                        evaluationCriteriaService.deleteCriterion(id)));
    }
}
