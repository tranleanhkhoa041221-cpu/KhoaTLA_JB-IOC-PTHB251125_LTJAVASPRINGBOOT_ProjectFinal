package ra.edu.service;

import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;
import ra.edu.dto.response.PaginationResponse;

public interface MentorService {

    PaginationResponse<MentorResponse> getAllMentors
            (int page,
             int size,
             String username,
             String fullName,
             String email,
             String phoneNumber,
             String department,
             String academicRank);

    MentorResponse getMentorById(Long id);

    PaginationResponse<MentorResponse> getAssignedMentors
            (int page,
             int size,
             String username,
             String fullName,
             String email,
             String phoneNumber,
             String department,
             String academicRank);

    MentorResponse getAssignedMentorById(Long mentorId);

    MentorResponse createMentor(MentorCreateRequest request);

    MentorResponse updateMentor(Long id, MentorUpdateRequest request);

}
