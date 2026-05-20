package ra.edu.service;

import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentFilterRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.StudentResponse;


public interface StudentService {

    PaginationResponse<StudentResponse> getAllStudents(StudentFilterRequest filter);

    StudentResponse getStudentById(Long id);

    StudentResponse createStudent(StudentCreateRequest request);

    StudentResponse updateStudent(Long id, StudentUpdateRequest request);
}
