package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.AssessmentRoundService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/assessment-rounds")
@RequiredArgsConstructor
public class AssessmentRoundController {

    private final AssessmentRoundService assessmentRoundService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String roundName,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Long phaseId,
            @RequestParam(required = false) String phaseName,
            @RequestParam(required = false) String isActive) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách đợt đánh giá thành công",
                        assessmentRoundService.getAllAssessmentRounds(
                                page,
                                size,
                                roundName,
                                startDate,
                                endDate,
                                description,
                                phaseId,
                                phaseName,
                                isActive)));
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
