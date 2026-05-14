package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.service.MentorService;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @GetMapping
    public ResponseEntity<ApiResponse<?>> getAll(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String academicRank
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách mentor thành công",
                        mentorService.getAllMentors(page, size, username, fullName, email, phoneNumber, department, academicRank))
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin mentor thành công",
                        mentorService.getMentorById(id))
        );
    }

    @GetMapping("/assigned")
    public ResponseEntity<ApiResponse<?>> getAssigned(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String phoneNumber,
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String academicRank
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách mentor được phân công thành công",
                        mentorService.getAssignedMentors(page, size,username, fullName, email, phoneNumber, department, academicRank))
        );
    }

    @GetMapping("/assigned/{id}")
    public ResponseEntity<ApiResponse<?>> getAssignedById(@PathVariable Long id) {
        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin mentor được phân công thành công",
                        mentorService.getAssignedMentorById(id))
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> create(
            @Valid @RequestBody MentorCreateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo mentor thành công",
                        mentorService.createMentor(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> update(
            @PathVariable Long id,
            @Valid @RequestBody MentorUpdateRequest request
    ) {
        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật mentor thành công",
                        mentorService.updateMentor(id, request))
        );
    }
}
