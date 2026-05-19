package ra.edu.service;

import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.StudentResponse;

import java.time.LocalDate;

public interface StudentService {

    PaginationResponse<StudentResponse> getAllStudents
            (int page,
             int size,
             String studentCode,
             String address,
             LocalDate dateOfBirth,
             String username,
             String fullName,
             String email,
             String phoneNumber,
             String major,
             String className);

    StudentResponse getStudentById(Long id);

    StudentResponse createStudent(StudentCreateRequest request);

    StudentResponse updateStudent(Long id, StudentUpdateRequest request);
}
