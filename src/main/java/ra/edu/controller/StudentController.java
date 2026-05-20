package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentFilterRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.StudentService;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @Valid @ModelAttribute StudentFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách sinh viên thành công",
                        studentService.getAllStudents(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết sinh viên thành công",
                        studentService.getStudentById(id)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody StudentCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo sinh viên thành công",
                        studentService.createStudent(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody StudentUpdateRequest request) {

        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật sinh viên thành công",
                        studentService.updateStudent(id, request)));
    }
}
