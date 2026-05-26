package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseFilterRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.service.InternshipPhaseService;


@RestController
@RequestMapping("/api/internship-phases")
@RequiredArgsConstructor
public class InternshipPhaseController {

    private final InternshipPhaseService internshipPhaseService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @Valid @ModelAttribute InternshipPhaseFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách giai đoạn thực tập thành công",
                        internshipPhaseService.getAllPhases(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết giai đoạn thực tập thành công",
                        internshipPhaseService.getPhaseById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody InternshipPhaseCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo giai đoạn thực tập thành công",
                        internshipPhaseService.createPhase(request)));
    }


    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody InternshipPhaseUpdateRequest request) {

        InternshipPhaseResponse response = internshipPhaseService.updatePhase(id, request);

        if (response == null) {

            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật giai đoạn thực tập thành công", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        internshipPhaseService.deletePhase(id);

        return ResponseEntity.noContent().build();

    }
}
