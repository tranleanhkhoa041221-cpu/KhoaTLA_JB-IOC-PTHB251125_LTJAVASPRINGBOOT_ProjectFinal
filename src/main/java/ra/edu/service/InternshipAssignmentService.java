package ra.edu.service;

import ra.edu.dto.request.InternshipAssignmentCreateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateStatusRequest;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.time.LocalDateTime;

public interface InternshipAssignmentService {

    PaginationResponse<InternshipAssignmentResponse> getAllAssignments(
            int page,
            int size,
            Long studentId,
            Long mentorId,
            Long phaseId,
            String studentUsername,
            String mentorUsername,
            String studentFullName,
            String mentorFullName,
            String studentEmail,
            String mentorEmail,
            String studentPhoneNumber,
            String mentorPhoneNumber,
            InternshipAssignmentsStatus status,
            LocalDateTime assignedDate,
            LocalDateTime minAssignedDate,
            LocalDateTime maxAssignedDate);

    InternshipAssignmentResponse getAssignmentById(Long id);

    InternshipAssignmentResponse createAssignment
            (InternshipAssignmentCreateRequest request);

    InternshipAssignmentResponse updateAssignment
            (Long id, InternshipAssignmentUpdateRequest request);

    InternshipAssignmentResponse updateAssignmentStatus
            (Long id, InternshipAssignmentUpdateStatusRequest request);

    InternshipAssignmentResponse deleteAssignment(Long id);
}
