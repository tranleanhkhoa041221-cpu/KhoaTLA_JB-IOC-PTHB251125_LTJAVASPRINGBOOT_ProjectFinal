package ra.edu.service;

import ra.edu.dto.request.InternshipAssignmentCreateRequest;
import ra.edu.dto.request.InternshipAssignmentFilterRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateStatusRequest;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.time.LocalDateTime;

public interface InternshipAssignmentService {

    PaginationResponse<InternshipAssignmentResponse> getAllAssignments(
            InternshipAssignmentFilterRequest filter);

    InternshipAssignmentResponse getAssignmentById(Long id);

    InternshipAssignmentResponse createAssignment
            (InternshipAssignmentCreateRequest request);

    InternshipAssignmentResponse updateAssignment
            (Long id, InternshipAssignmentUpdateRequest request);

    InternshipAssignmentResponse updateAssignmentStatus
            (Long id, InternshipAssignmentUpdateStatusRequest request);

    void deleteAssignment(Long id);
}
