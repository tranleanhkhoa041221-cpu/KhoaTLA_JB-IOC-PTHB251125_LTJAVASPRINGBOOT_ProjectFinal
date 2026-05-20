package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.EvaluationCriteriaCreateRequest;
import ra.edu.dto.request.EvaluationCriteriaFilterRequest;
import ra.edu.dto.request.EvaluationCriteriaUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.EvaluationCriteriaService;


@RestController
@RequestMapping("/api/evaluation-criteria")
@RequiredArgsConstructor
public class EvaluationCriteriaController {

    private final EvaluationCriteriaService evaluationCriteriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @Valid @ModelAttribute EvaluationCriteriaFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách tiêu chí đánh giá thành công",
                        evaluationCriteriaService.getAllCriteria(filter)));
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
