package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.AssessmentRound;
import ra.edu.entity.InternshipPhase;
import ra.edu.mapper.AssessmentRoundMapper;
import ra.edu.repository.AssessmentRoundRepository;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.service.AssessmentRoundService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentRoundServiceImpl implements AssessmentRoundService {

    private final AssessmentRoundRepository assessmentRoundRepository;

    private final InternshipPhaseRepository internshipPhaseRepository;

    private final AssessmentRoundMapper assessmentRoundMapper;

    @Override
    public PaginationResponse<AssessmentRoundResponse> getAllAssessmentRounds(
            int page,
            int size,
            String roundName,
            LocalDate startDate,
            LocalDate endDate,
            String description,
            Long phaseId,
            String phaseName,
            String isActive) {

        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {

            throw new IllegalArgumentException("Ngày bắt đầu đợt đánh giá không được sau ngày kết thúc đợt đánh giá");
        }

        if (phaseId != null && !internshipPhaseRepository.existsById(phaseId)) {

            throw new EntityNotFoundException("Không tìm thấy Phase với ID = " + phaseId);
        }

        Boolean active = null;

        if (isActive != null && !isActive.isBlank()) {

            if (!isActive.equalsIgnoreCase("true")
                    && !isActive.equalsIgnoreCase("false")) {

                throw new IllegalArgumentException(
                        "isActive không hợp lệ. Chỉ được true hoặc false");
            }

            active = Boolean.parseBoolean(isActive);
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("roundId").descending());

        Page<AssessmentRound> roundPage;

        if (roundName != null && !roundName.isBlank()) {

            roundPage = assessmentRoundRepository
                    .findAllByRoundNameContainingIgnoreCase(roundName, pageable);

        } else if (startDate != null && endDate != null) {

            roundPage = assessmentRoundRepository
                    .findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual
                            (startDate, endDate, pageable);

        } else if (startDate != null) {

            roundPage = assessmentRoundRepository
                    .findAllByStartDate(startDate, pageable);

        } else if (endDate != null) {

            roundPage = assessmentRoundRepository
                    .findAllByEndDate(endDate, pageable);

        } else if (description != null && !description.isBlank()) {

            roundPage = assessmentRoundRepository
                    .findAllByDescriptionContainingIgnoreCase(description, pageable);

        } else if (phaseId != null) {

            roundPage = assessmentRoundRepository
                    .findAllByPhase_PhaseId(phaseId, pageable);

        } else if (phaseName != null && !phaseName.isBlank()) {

            roundPage = assessmentRoundRepository
                    .findAllByPhase_PhaseNameContainingIgnoreCase
                            (phaseName, pageable);

        } else if (active != null) {

            roundPage = assessmentRoundRepository
                    .findAllByIsActive(active, pageable);

        } else {

            roundPage = assessmentRoundRepository
                    .findAll(pageable);
        }

        List<AssessmentRoundResponse> items = roundPage.getContent()
                .stream()
                .map(assessmentRoundMapper::toResponse)
                .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(roundPage.getTotalPages())
                .totalItems(roundPage.getTotalElements())
                .build();

        return PaginationResponse.<AssessmentRoundResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public AssessmentRoundResponse getAssessmentRoundById(Long id) {

        AssessmentRound round = assessmentRoundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đợt đánh giá với ID = " + id));

        return assessmentRoundMapper.toResponse(round);
    }

    @Override
    public AssessmentRoundResponse createAssessmentRound(AssessmentRoundCreateRequest request) {

        InternshipPhase phase = internshipPhaseRepository.findById(request.getPhaseId())
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy Phase với ID = " + request.getPhaseId()));

        if (assessmentRoundRepository.existsByRoundNameIgnoreCaseAndPhase_PhaseId(request.getRoundName(), phase.getPhaseId())) {

            throw new IllegalStateException("Tên đợt đánh giá : '" + request.getRoundName() +
                    "' đã tồn tại trong Phase ID = " + phase.getPhaseId());
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {

            throw new IllegalArgumentException("Ngày bắt đầu đợt đánh giá không được sau ngày kết thúc đợt đánh giá");
        }

        AssessmentRound round = assessmentRoundMapper.toEntity(request);

        round.setPhase(phase);
        round.setIsActive(true);
        round.setCreatedAt(LocalDateTime.now());
        round.setUpdatedAt(LocalDateTime.now());

        assessmentRoundRepository.save(round);

        return assessmentRoundMapper.toResponse(round);
    }

    @Override
    public AssessmentRoundResponse updateAssessmentRound(Long id, AssessmentRoundUpdateRequest request) {

        AssessmentRound round = assessmentRoundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy đợt đánh giá với ID = " + id));

        boolean used = (round.getRoundCriteria() != null && !round.getRoundCriteria().isEmpty())
                || (round.getAssessmentResults() != null && !round.getAssessmentResults().isEmpty());

        if (request.getRoundName() != null
                && !request.getRoundName().equalsIgnoreCase(round.getRoundName())) {
            if (used)
                throw new IllegalStateException("Đợt đánh giá đã được sử dụng, không thể đổi tên đợt đánh giá");

            if (assessmentRoundRepository.existsByRoundNameIgnoreCaseAndPhase_PhaseId(request.getRoundName(), round.getPhase().getPhaseId())) {

                throw new IllegalStateException("Tên đợt đánh giá : '" + request.getRoundName() +
                        "' đã tồn tại trong Phase ID = " + round.getPhase().getPhaseId());
            }
        }

        if (request.getStartDate() != null && !request.getStartDate().equals(round.getStartDate()) && used) {

            throw new IllegalStateException("Đợt đánh giá đã được sử dụng, không thể đổi ngày bắt đầu đợt đánh giá");
        }

        if (request.getEndDate() != null && !request.getEndDate().equals(round.getEndDate()) && used) {

            throw new IllegalStateException("Đợt đánh giá đã được sử dụng, không thể đổi ngày kết thúc đợt đánh giá");
        }

        LocalDate newStartDate = request.getStartDate() != null
                ? request.getStartDate()
                : round.getStartDate();

        LocalDate newEndDate = request.getEndDate() != null
                ? request.getEndDate()
                : round.getEndDate();

        if (newStartDate.isAfter(newEndDate)) {

            throw new IllegalArgumentException("Ngày bắt đầu đợt đánh giá không được sau ngày kết thúc đợt đánh giá");
        }

        assessmentRoundMapper.updateEntityFromDto(request, round);

        if (request.getIsActive() != null) {

            Boolean newActive = Boolean.parseBoolean(request.getIsActive());

            if (!newActive.equals(round.getIsActive()) && used) {

                throw new IllegalStateException("Đợt đánh giá đã được sử dụng, không thể đổi trạng thái hoạt động của đợt đánh giá");
            }

            round.setIsActive(newActive);
        }

        round.setUpdatedAt(LocalDateTime.now());

        assessmentRoundRepository.save(round);

        return assessmentRoundMapper.toResponse(round);
    }

    @Override
    public AssessmentRoundResponse deleteAssessmentRound(Long id) {

        AssessmentRound round = assessmentRoundRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Không tìm thấy đợt đánh giá với ID = " + id));

        if (round.getRoundCriteria() != null && !round.getRoundCriteria().isEmpty()) {

            throw new IllegalStateException(
                    "Không thể xóa đợt đánh giá ID = " + id +
                            " vì đã liên kết với RoundCriteria");
        }

        if (round.getAssessmentResults() != null && !round.getAssessmentResults().isEmpty()) {

            throw new IllegalStateException(
                    "Không thể xóa đợt đánh giá ID = " + id +
                            " vì đã liên kết với AssessmentResult");
        }

        AssessmentRoundResponse response = assessmentRoundMapper.toResponse(round);

        assessmentRoundRepository.delete(round);

        return response;


    }
}
