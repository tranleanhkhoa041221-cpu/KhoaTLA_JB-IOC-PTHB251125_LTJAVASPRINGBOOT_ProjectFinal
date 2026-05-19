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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long roundId,
            @RequestParam(required = false) Long criterionId,
            @RequestParam(required = false) String roundName,
            @RequestParam(required = false) String criterionName,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = false,
                    message = "Weight phải lớn hơn 0"
            )
            @DecimalMax(
                    value = "1.0",
                    inclusive = true,
                    message = "Weight phải nhỏ hơn hoặc bằng 1"
            )
            BigDecimal weight,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = false,
                    message = "minWeight phải lớn hơn 0"
            )
            @DecimalMax(
                    value = "1.0",
                    inclusive = true,
                    message = "minWeight phải nhỏ hơn hoặc bằng 1"
            )
            BigDecimal minWeight,
            @RequestParam(required = false)
            @DecimalMin(
                    value = "0.0",
                    inclusive = false,
                    message = "maxWeight phải lớn hơn 0"
            )
            @DecimalMax(
                    value = "1.0",
                    inclusive = true,
                    message = "maxWeight phải nhỏ hơn hoặc bằng 1"
            )
            BigDecimal maxWeight) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách tiêu chí trong đợt đánh giá thành công",
                        roundCriteriaService.getAllRoundCriteria(
                                page,
                                size,
                                roundId,
                                criterionId,
                                roundName,
                                criterionName,
                                weight,
                                minWeight,
                                maxWeight)));
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
