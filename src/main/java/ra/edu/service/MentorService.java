package ra.edu.service;

import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorFilterRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;
import ra.edu.dto.response.PaginationResponse;

public interface MentorService {

    PaginationResponse<MentorResponse> getAllMentors(MentorFilterRequest filter);

    MentorResponse getMentorById(Long id);

    PaginationResponse<MentorResponse> getAssignedMentors(MentorFilterRequest filter);

    MentorResponse getAssignedMentorById(Long mentorId);

    MentorResponse createMentor(MentorCreateRequest request);

    MentorResponse updateMentor(Long id, MentorUpdateRequest request);

}
