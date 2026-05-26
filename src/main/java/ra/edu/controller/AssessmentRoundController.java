package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundFilterRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.request.AssessmentRoundUpdateStatusRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.service.AssessmentRoundService;


@RestController
@RequestMapping("/api/assessment-rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

    private final AssessmentRoundService assessmentRoundService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @Valid @ModelAttribute AssessmentRoundFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách đợt đánh giá thành công",
                        assessmentRoundService.getAllAssessmentRounds(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết đợt đánh giá thành công",
                        assessmentRoundService.getAssessmentRoundById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody AssessmentRoundCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "Tạo đợt đánh giá thành công",
                                assessmentRoundService.createAssessmentRound(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentRoundUpdateRequest request) {

        AssessmentRoundResponse response = assessmentRoundService.updateAssessmentRound(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật đợt đánh giá thành công", response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody AssessmentRoundUpdateStatusRequest request) {

        AssessmentRoundResponse response = assessmentRoundService.updateAssessmentRoundStatus(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật trạng thái đợt đánh giá thành công",
                        response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {

        assessmentRoundService.deleteAssessmentRound(id);

        return ResponseEntity.noContent().build();
    }
}
