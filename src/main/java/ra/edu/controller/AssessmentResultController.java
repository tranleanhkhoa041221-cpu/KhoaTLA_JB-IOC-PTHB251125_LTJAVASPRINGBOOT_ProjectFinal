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
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.service.AssessmentResultService;


@RestController
@RequestMapping("/api/assessment-results")
@RequiredArgsConstructor
public class AssessmentResultController {

    private final AssessmentResultService assessmentResultService;

    @GetMapping
    public ResponseEntity<?> getAll(
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
    public ResponseEntity<?> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết AssessmentResult thành công",
                        assessmentResultService.getResultById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody AssessmentResultCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "Tạo AssessmentResult thành công",
                                assessmentResultService.createResult(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentResultUpdateRequest request) {

        AssessmentResultResponse response = assessmentResultService.updateResult(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật AssessmentResult thành công", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {

        assessmentResultService.deleteResult(id);

        return ResponseEntity.noContent().build();
    }


}
