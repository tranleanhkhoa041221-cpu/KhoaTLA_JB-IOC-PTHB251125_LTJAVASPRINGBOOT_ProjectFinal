package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.InternshipAssignmentCreateRequest;
import ra.edu.dto.request.InternshipAssignmentFilterRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateStatusRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.service.InternshipAssignmentService;


@RestController
@RequestMapping("/api/internship-assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

    private final InternshipAssignmentService internshipAssignmentService;

    @GetMapping
    public ResponseEntity<?> getAllAssignments(
            @Valid @ModelAttribute InternshipAssignmentFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách phân công thực tập thành công",
                        internshipAssignmentService.getAllAssignments(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssignmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết phân công thực tập thành công",
                        internshipAssignmentService.getAssignmentById(id)));
    }

    @PostMapping
    public ResponseEntity<?> createAssignment(
            @Valid
            @RequestBody
            InternshipAssignmentCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.created(
                                "Tạo phân công thực tập thành công",
                                internshipAssignmentService
                                        .createAssignment(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody InternshipAssignmentUpdateRequest request) {

        InternshipAssignmentResponse response = internshipAssignmentService.updateAssignment(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật phân công thực tập thành công",
                        response));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateAssignmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody InternshipAssignmentUpdateStatusRequest request) {

        InternshipAssignmentResponse response = internshipAssignmentService.updateAssignmentStatus(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật trạng thái phân công thực tập thành công",
                        response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssignment(
            @PathVariable Long id) {

        internshipAssignmentService.deleteAssignment(id);

        return ResponseEntity.noContent().build();
    }
}
