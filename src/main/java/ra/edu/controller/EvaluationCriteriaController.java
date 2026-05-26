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
import ra.edu.dto.response.EvaluationCriteriaResponse;
import ra.edu.service.EvaluationCriteriaService;


@RestController
@RequestMapping("/api/evaluation-criteria")
@RequiredArgsConstructor
public class EvaluationCriteriaController {

    private final EvaluationCriteriaService evaluationCriteriaService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @Valid @ModelAttribute EvaluationCriteriaFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách tiêu chí đánh giá thành công",
                        evaluationCriteriaService.getAllCriteria(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết tiêu chí đánh giá thành công",
                        evaluationCriteriaService.getCriterionById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody EvaluationCriteriaCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo tiêu chí đánh giá thành công",
                        evaluationCriteriaService.createCriterion(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody EvaluationCriteriaUpdateRequest request) {

        EvaluationCriteriaResponse response = evaluationCriteriaService.updateCriterion(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật tiêu chí đánh giá thành công", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        evaluationCriteriaService.deleteCriterion(id);

        return ResponseEntity.noContent().build();
    }
}
