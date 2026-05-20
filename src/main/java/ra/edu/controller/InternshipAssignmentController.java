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
import ra.edu.service.InternshipAssignmentService;


@RestController
@RequestMapping("/api/internship-assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

    private final InternshipAssignmentService internshipAssignmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllAssignments(
            @Valid @ModelAttribute InternshipAssignmentFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách phân công thực tập thành công",
                        internshipAssignmentService.getAllAssignments(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getAssignmentById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy thông tin chi tiết phân công thực tập thành công",
                        internshipAssignmentService.getAssignmentById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> createAssignment(
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
    public ResponseEntity<ApiResponse<?>> updateAssignment(
            @PathVariable Long id,
            @Valid @RequestBody InternshipAssignmentUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật phân công thực tập thành công",
                        internshipAssignmentService
                                .updateAssignment(id, request)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<?>> updateAssignmentStatus(
            @PathVariable Long id,
            @Valid @RequestBody InternshipAssignmentUpdateStatusRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Cập nhật trạng thái phân công thực tập thành công",
                        internshipAssignmentService
                                .updateAssignmentStatus(id, request)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAssignment(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Xóa phân công thực tập thành công",
                        internshipAssignmentService.deleteAssignment(id)));
    }
}
