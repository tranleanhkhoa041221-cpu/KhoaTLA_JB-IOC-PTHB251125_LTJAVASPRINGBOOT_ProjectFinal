package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaFilterRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.RoundCriteriaResponse;
import ra.edu.service.RoundCriteriaService;


@RestController
@RequestMapping("/api/round-criteria")
@RequiredArgsConstructor
@Validated
public class RoundCriteriaController {

    private final RoundCriteriaService roundCriteriaService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @Valid @ModelAttribute RoundCriteriaFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách tiêu chí trong đợt đánh giá thành công",
                        roundCriteriaService.getAllRoundCriteria(filter)));
    }

    @GetMapping("/round/{roundId}")
    public ResponseEntity<?> getCriteriaByRoundId(
            @PathVariable Long roundId) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách tiêu chí theo đợt đánh giá thành công",
                        roundCriteriaService.getCriteriaByRoundId(roundId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết tiêu chí trong đợt đánh giá thành công",
                        roundCriteriaService.getRoundCriteriaById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody RoundCriteriaCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "Tạo tiêu chí trong đợt đánh giá thành công",
                                roundCriteriaService.createRoundCriteria(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody RoundCriteriaUpdateRequest request) {

        RoundCriteriaResponse response = roundCriteriaService.updateRoundCriteria(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật tiêu chí trong đợt đánh giá thành công", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        roundCriteriaService.deleteRoundCriteria(id);

        return ResponseEntity.noContent().build();
    }
}
