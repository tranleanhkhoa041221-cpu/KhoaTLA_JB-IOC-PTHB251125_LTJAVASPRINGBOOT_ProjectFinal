package ra.edu.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.InternshipAssignmentCreateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateStatusRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.entity.InternshipAssignmentsStatus;
import ra.edu.service.InternshipAssignmentService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/internship-assignments")
@RequiredArgsConstructor
public class InternshipAssignmentController {

    private final InternshipAssignmentService internshipAssignmentService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAllAssignments(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long studentId,
            @RequestParam(required = false) Long mentorId,
            @RequestParam(required = false) Long phaseId,
            @RequestParam(required = false) String studentUsername,
            @RequestParam(required = false) String mentorUsername,
            @RequestParam(required = false) String studentFullName,
            @RequestParam(required = false) String mentorFullName,
            @RequestParam(required = false) String studentEmail,
            @RequestParam(required = false) String mentorEmail,
            @RequestParam(required = false) String studentPhoneNumber,
            @RequestParam(required = false) String mentorPhoneNumber,
            @RequestParam(required = false) InternshipAssignmentsStatus status,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime assignedDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime minAssignedDate,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm") LocalDateTime maxAssignedDate) {

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Lấy danh sách phân công thực tập thành công",
                        internshipAssignmentService.getAllAssignments(
                                page,
                                size,
                                studentId,
                                mentorId,
                                phaseId,
                                studentUsername,
                                mentorUsername,
                                studentFullName,
                                mentorFullName,
                                studentEmail,
                                mentorEmail,
                                studentPhoneNumber,
                                mentorPhoneNumber,
                                status,
                                assignedDate,
                                minAssignedDate,
                                maxAssignedDate)));
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
