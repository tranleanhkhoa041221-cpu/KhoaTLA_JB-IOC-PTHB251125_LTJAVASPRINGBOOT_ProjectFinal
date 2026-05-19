package ra.edu.service;

import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.dto.response.PaginationResponse;

import java.time.LocalDate;

public interface InternshipPhaseService {

    PaginationResponse<InternshipPhaseResponse> getAllPhases(
            int page,
            int size,
            String phaseName,
            LocalDate startDate,
            LocalDate endDate,
            String description);

    InternshipPhaseResponse getPhaseById(Long id);

    InternshipPhaseResponse createPhase
            (InternshipPhaseCreateRequest request);

    InternshipPhaseResponse updatePhase
            (Long id, InternshipPhaseUpdateRequest request);

    InternshipPhaseResponse deletePhase(Long id);

}
