package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.StudentCreateRequest;
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
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String studentCode,
            @RequestParam(required = false) String address,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate dateOfBirth,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String major,
            @RequestParam(required = false) String className) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách sinh viên thành công",
                        studentService.getAllStudents(page, size, studentCode, address, dateOfBirth, username, fullName, email, phoneNumber, major, className)));
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
