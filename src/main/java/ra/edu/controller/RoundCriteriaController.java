package ra.edu.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaFilterRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.RoundCriteriaService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/round-criteria")
@RequiredArgsConstructor
@Validated
public class RoundCriteriaController {

    private final RoundCriteriaService roundCriteriaService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @Valid @ModelAttribute RoundCriteriaFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách tiêu chí trong đợt đánh giá thành công",
                        roundCriteriaService.getAllRoundCriteria(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết tiêu chí trong đợt đánh giá thành công",
                        roundCriteriaService.getRoundCriteriaById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody RoundCriteriaCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "Tạo tiêu chí trong đợt đánh giá thành công",
                                roundCriteriaService.createRoundCriteria(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody RoundCriteriaUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật tiêu chí trong đợt đánh giá thành công",
                        roundCriteriaService.updateRoundCriteria(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Xóa tiêu chí trong đợt đánh giá thành công",
                        roundCriteriaService.deleteRoundCriteria(id)));
    }
}
