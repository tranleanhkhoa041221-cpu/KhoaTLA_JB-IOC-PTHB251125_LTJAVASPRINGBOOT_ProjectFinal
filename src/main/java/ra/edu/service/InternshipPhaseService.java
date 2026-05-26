package ra.edu.service;

import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseFilterRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.dto.response.PaginationResponse;

import java.time.LocalDate;

public interface InternshipPhaseService {

    PaginationResponse<InternshipPhaseResponse> getAllPhases(
            InternshipPhaseFilterRequest request);

    InternshipPhaseResponse getPhaseById(Long id);

    InternshipPhaseResponse createPhase
            (InternshipPhaseCreateRequest request);

    InternshipPhaseResponse updatePhase
            (Long id, InternshipPhaseUpdateRequest request);

    void deletePhase(Long id);

}
