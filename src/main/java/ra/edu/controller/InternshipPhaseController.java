package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.InternshipPhaseService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/internship-phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

    private final InternshipPhaseService internshipPhaseService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String phaseName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate,
            @RequestParam(required = false) String description
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách giai đoạn thực tập thành công",
                        internshipPhaseService.getAllPhases(page, size, phaseName, startDate, endDate, description))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin giai đoạn thực tập thành công",
                        internshipPhaseService.getPhaseById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody InternshipPhaseCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo giai đoạn thực tập thành công",
                        internshipPhaseService.createPhase(request)));
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody InternshipPhaseUpdateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật giai đoạn thực tập thành công",
                        internshipPhaseService.updatePhase(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> delete(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Xóa giai đoạn thực tập thành công", internshipPhaseService.deletePhase(id))
        );
    }
}
