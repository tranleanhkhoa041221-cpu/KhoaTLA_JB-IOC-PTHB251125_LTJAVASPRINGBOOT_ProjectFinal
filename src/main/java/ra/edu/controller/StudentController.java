package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentFilterRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.StudentResponse;
import ra.edu.service.StudentService;


@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @Valid @ModelAttribute StudentFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách sinh viên thành công",
                        studentService.getAllStudents(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết sinh viên thành công",
                        studentService.getStudentById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody StudentCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo sinh viên thành công",
                        studentService.createStudent(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {

        StudentResponse response = studentService.updateStudent(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật sinh viên thành công", response));
    }
}
