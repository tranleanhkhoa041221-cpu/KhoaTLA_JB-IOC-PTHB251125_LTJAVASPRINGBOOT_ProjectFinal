package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssessmentResultCreateRequest;
import ra.edu.dto.request.AssessmentResultFilterRequest;
import ra.edu.dto.request.AssessmentResultUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.AssessmentResultService;


@RestController
@RequestMapping("/api/assessment-results")
@RequiredArgsConstructor
public class AssessmentResultController {

    private final AssessmentResultService assessmentResultService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @Valid @ModelAttribute AssessmentResultFilterRequest filter
    ) {
        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách AssessmentResult thành công",
                        assessmentResultService.getAllResult(filter)
                )
        );
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
