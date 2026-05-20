package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundFilterRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.AssessmentRoundService;


@RestController
@RequestMapping("/api/assessment-rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

    private final AssessmentRoundService assessmentRoundService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @Valid @ModelAttribute AssessmentRoundFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách đợt đánh giá thành công",
                        assessmentRoundService.getAllAssessmentRounds(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết đợt đánh giá thành công",
                        assessmentRoundService.getAssessmentRoundById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody AssessmentRoundCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "Tạo đợt đánh giá thành công",
                                assessmentRoundService.createAssessmentRound(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentRoundUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật đợt đánh giá thành công",
                        assessmentRoundService.updateAssessmentRound(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Xóa đợt đánh giá thành công",
                        assessmentRoundService.deleteAssessmentRound(id)));
    }
}
