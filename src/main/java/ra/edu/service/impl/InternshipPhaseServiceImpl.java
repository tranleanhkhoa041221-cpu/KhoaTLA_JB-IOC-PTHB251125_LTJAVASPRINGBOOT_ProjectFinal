package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.InternshipPhase;
import ra.edu.mapper.InternshipPhaseMapper;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.service.InternshipPhaseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    private final InternshipPhaseMapper internshipPhaseMapper;

    @Override
    public PaginationResponse<InternshipPhaseResponse> getAllPhases(
            int page,
            int size,
            String phaseName,
            LocalDate startDate,
            LocalDate endDate,
            String description) {

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {

            throw new IllegalArgumentException(
                    "Ngày bắt đầu giai đoạn thực tập không được sau ngày kết thúc giai đoạn thực tập");
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("phaseId").descending());

        Page<InternshipPhase> phasePage;

        if (phaseName != null && !phaseName.isBlank()) {

            phasePage = internshipPhaseRepository
                    .findAllByPhaseNameContainingIgnoreCase(phaseName, pageable);

        } else if (startDate != null && endDate != null) {

            phasePage = internshipPhaseRepository
                    .findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual
                            (startDate, endDate, pageable);

        } else if (startDate != null) {

            phasePage = internshipPhaseRepository
                    .findAllByStartDate(startDate, pageable);

        } else if (endDate != null) {

            phasePage = internshipPhaseRepository
                    .findAllByEndDate(endDate, pageable);

        } else if (description != null && !description.isBlank()) {

            phasePage = internshipPhaseRepository
                    .findAllByDescriptionContainingIgnoreCase
                            (description, pageable);

        } else {

            phasePage = internshipPhaseRepository
                    .findAll(pageable);
        }

        List<InternshipPhaseResponse> items = phasePage.getContent()
                .stream()
                .map(internshipPhaseMapper::toResponse)
                .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(phasePage.getTotalPages())
                .totalItems(phasePage.getTotalElements())
                .build();

        return PaginationResponse.<InternshipPhaseResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Long id) {

        InternshipPhase phase = internshipPhaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Phase với ID = " + id));

        return internshipPhaseMapper.toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse createPhase
            (InternshipPhaseCreateRequest request) {

        if (internshipPhaseRepository
                .existsByPhaseNameIgnoreCase(request.getPhaseName())) {

            throw new IllegalStateException("Tên giai đoạn thực tập đã tồn tại");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {

            throw new IllegalArgumentException(
                    "Ngày bắt đầu giai đoạn thực tập không được sau ngày kết thúc giai đoạn thực tập");
        }

        InternshipPhase phase = internshipPhaseMapper.toEntity(request);

        phase.setCreatedAt(LocalDateTime.now());
        phase.setUpdatedAt(LocalDateTime.now());

        internshipPhaseRepository.save(phase);

        return internshipPhaseMapper.toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse updatePhase(
            Long id,
            InternshipPhaseUpdateRequest request) {

        InternshipPhase phase =
                internshipPhaseRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy Phase với ID = " + id));

        boolean used =
                (phase.getInternshipAssignments() != null
                        && !phase.getInternshipAssignments().isEmpty())
                        || (phase.getAssessmentRounds() != null
                        && !phase.getAssessmentRounds().isEmpty());


        if (request.getPhaseName() != null
                && !request.getPhaseName().equalsIgnoreCase(phase.getPhaseName())) {

            if (used) {

                throw new IllegalStateException(
                        "Phase đã được sử dụng, không thể thay đổi tên giai đoạn thực tập");
            }

            if (internshipPhaseRepository.existsByPhaseNameIgnoreCase(
                    request.getPhaseName())) {

                throw new IllegalStateException(
                        "Tên giai đoạn thực tập đã tồn tại");
            }
        }

        if (request.getStartDate() != null && !request.getStartDate()
                .equals(phase.getStartDate()) && used) {

            throw new IllegalStateException(
                    "Phase đã được sử dụng, không thể thay đổi ngày bắt đầu giai đoạn thực tập");
        }

        if (request.getEndDate() != null && !request.getEndDate()
                .equals(phase.getEndDate()) && used) {

            throw new IllegalStateException(
                    "Phase đã được sử dụng, không thể thay đổi ngày kết thúc giai đoạn thực tập");
        }

        LocalDate newStartDate = request.getStartDate() != null
                ? request.getStartDate()
                : phase.getStartDate();

        LocalDate newEndDate = request.getEndDate() != null
                ? request.getEndDate()
                : phase.getEndDate();

        if (newStartDate.isAfter(newEndDate)) {

            throw new IllegalArgumentException(
                    "Ngày bắt đầu giai đoạn thực tập không được sau ngày kết thúc giai đoạn thực tập");
        }

        internshipPhaseMapper.updateEntityFromDto(request, phase);

        phase.setUpdatedAt(LocalDateTime.now());

        internshipPhaseRepository.save(phase);

        return internshipPhaseMapper.toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse deletePhase(Long id) {

        InternshipPhase phase = internshipPhaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Phase với ID = " + id));

        if (phase.getInternshipAssignments() != null
                && !phase.getInternshipAssignments().isEmpty()) {
            throw new IllegalStateException("Không thể xóa Phase ID = " + id + " vì đã liên kết với InternshipAssignment");
        }

        if (phase.getAssessmentRounds() != null
                && !phase.getAssessmentRounds().isEmpty()) {
            throw new IllegalStateException("Không thể xóa Phase ID = " + id + " vì đã liên kết với AssessmentRound");
        }

        InternshipPhaseResponse response = internshipPhaseMapper.toResponse(phase);

        internshipPhaseRepository.delete(phase);

        return response;
    }


}
