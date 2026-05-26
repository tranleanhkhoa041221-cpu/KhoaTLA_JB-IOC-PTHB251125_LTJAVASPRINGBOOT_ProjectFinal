package ra.edu.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorFilterRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.ApiResponse;
import ra.edu.dto.response.MentorResponse;
import ra.edu.service.MentorService;

@RestController
@RequestMapping("/api/mentors")
@RequiredArgsConstructor
public class MentorController {

    private final MentorService mentorService;

    @GetMapping
    public ResponseEntity<?> getAll(
            @Valid @ModelAttribute MentorFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách mentor thành công",
                        mentorService.getAllMentors(filter)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết mentor thành công",
                        mentorService.getMentorById(id)));
    }

    @GetMapping("/assigned")
    public ResponseEntity<?> getAssigned(
            @Valid @ModelAttribute MentorFilterRequest filter) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy danh sách mentor được phân công thành công",
                        mentorService.getAssignedMentors(filter)));
    }

    @GetMapping("/assigned/{id}")
    public ResponseEntity<?> getAssignedById(@PathVariable Long id) {

        return ResponseEntity.ok(
                ApiResponse.success("Lấy thông tin chi tiết mentor được phân công thành công",
                        mentorService.getAssignedMentorById(id)));
    }

    @PostMapping
    public ResponseEntity<?> create(
            @Valid @RequestBody MentorCreateRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Tạo mentor thành công",
                        mentorService.createMentor(request)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody MentorUpdateRequest request) {

        MentorResponse response = mentorService.updateMentor(id, request);

        if (response == null) {
            return ResponseEntity.noContent().build();
        }


        return ResponseEntity.ok(
                ApiResponse.success("Cập nhật mentor thành công", response));
    }
}
