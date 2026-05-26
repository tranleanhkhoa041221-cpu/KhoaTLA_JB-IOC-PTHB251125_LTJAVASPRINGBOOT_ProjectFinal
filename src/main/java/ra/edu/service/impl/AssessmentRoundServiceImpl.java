package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.edu.config.principal.UserPrincipal;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundFilterRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.request.AssessmentRoundUpdateStatusRequest;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.AssessmentRound;
import ra.edu.entity.InternshipPhase;
import ra.edu.entity.User;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.NotFoundException;
import ra.edu.helper.AccessValidator;
import ra.edu.mapper.AssessmentRoundMapper;
import ra.edu.repository.AssessmentRoundRepository;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.service.AssessmentRoundService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentRoundServiceImpl implements AssessmentRoundService {

    private final AssessmentRoundRepository assessmentRoundRepository;

    private final InternshipPhaseRepository internshipPhaseRepository;

    private final AssessmentRoundMapper assessmentRoundMapper;

    private final AccessValidator accessValidator;


    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<AssessmentRoundResponse> getAllAssessmentRounds(
            AssessmentRoundFilterRequest request) {

        accessValidator.validateAccess(getCurrentUser());

        if (request.getStartDate() != null
                && request.getEndDate() != null
                && request.getStartDate()
                .isAfter(request.getEndDate())) {

            throw new BadRequestException(
                    "Ngày bắt đầu đợt đánh giá không được sau ngày kết thúc đợt đánh giá");
        }

        if (request.getPhaseId() != null
                && !internshipPhaseRepository
                .existsById(request.getPhaseId())) {

            throw new NotFoundException(
                    "Không tìm thấy Phase với ID = "
                            + request.getPhaseId());
        }

        Boolean active = null;

        if (request.getIsActive() != null && !request.getIsActive().isBlank()) {

            if (!request.getIsActive().equalsIgnoreCase("true")
                    && !request.getIsActive().equalsIgnoreCase("false")) {

                throw new BadRequestException(
                        "isActive không hợp lệ. Chỉ được true hoặc false");
            }

            active = Boolean.parseBoolean(request.getIsActive());
        }


        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by("roundId").descending());

        Page<AssessmentRound> roundPage;

        if (request.getRoundName() != null
                && !request.getRoundName().isBlank()) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByRoundNameContainingIgnoreCase(
                                    request.getRoundName(),
                                    pageable);

        } else if (request.getStartDate() != null
                && request.getEndDate() != null) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(
                                    request.getStartDate(),
                                    request.getEndDate(),
                                    pageable);

        } else if (request.getStartDate() != null) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByStartDate(
                                    request.getStartDate(),
                                    pageable);

        } else if (request.getEndDate() != null) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByEndDate(
                                    request.getEndDate(),
                                    pageable);


        } else if (request.getDescription() != null
                && !request.getDescription().isBlank()) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByDescriptionContainingIgnoreCase(
                                    request.getDescription(),
                                    pageable);

        } else if (request.getPhaseId() != null) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByPhase_PhaseId(
                                    request.getPhaseId(),
                                    pageable);

        } else if (request.getPhaseName() != null
                && !request.getPhaseName().isBlank()) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByPhase_PhaseNameContainingIgnoreCase(
                                    request.getPhaseName(),
                                    pageable);

        } else if (active != null) {

            roundPage =
                    assessmentRoundRepository
                            .findAllByIsActive(
                                    active,
                                    pageable);

        } else {

            roundPage =
                    assessmentRoundRepository
                            .findAll(pageable);
        }

        List<AssessmentRoundResponse> items =
                roundPage.getContent()
                        .stream()
                        .map(assessmentRoundMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalPages(roundPage.getTotalPages())
                .totalItems(roundPage.getTotalElements())
                .build();

        return PaginationResponse
                .<AssessmentRoundResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public AssessmentRoundResponse getAssessmentRoundById(Long id) {

        accessValidator.validateAccess(getCurrentUser());

        AssessmentRound round = assessmentRoundRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đợt đánh giá với ID = " + id));

        return assessmentRoundMapper.toResponse(round);
    }

    @Override
    public AssessmentRoundResponse createAssessmentRound(
            AssessmentRoundCreateRequest request) {

        InternshipPhase phase =
                internshipPhaseRepository
                        .findById(request.getPhaseId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy Phase với ID = "
                                                + request.getPhaseId()));

        LocalDate now = LocalDate.now();

        if (now.isBefore(phase.getStartDate())
                || now.isAfter(phase.getEndDate())) {

            throw new BadRequestException(
                    "Hiện tại không nằm trong thời gian của InternshipPhase ID = "
                            + phase.getPhaseId()
                            + " | startDate = "
                            + phase.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " | endDate = "
                            + phase.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (assessmentRoundRepository
                .existsByRoundNameIgnoreCaseAndPhase_PhaseId(
                        request.getRoundName(),
                        phase.getPhaseId())) {

            throw new ConflictException(
                    "Tên đợt đánh giá : '"
                            + request.getRoundName()
                            + "' đã tồn tại trong Phase ID = "
                            + phase.getPhaseId());
        }

        if (request.getStartDate()
                .isAfter(request.getEndDate())) {

            throw new BadRequestException(
                    "Ngày bắt đầu đợt đánh giá không được sau ngày kết thúc đợt đánh giá");
        }

        if (request.getStartDate().isBefore(phase.getStartDate()) || request.getEndDate().isAfter(phase.getEndDate())) {
            throw new BadRequestException(
                    "Thời gian đợt đánh giá phải nằm trong khoảng thời gian của InternshipPhase ("
                            + phase.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " đến " + phase.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        }


        AssessmentRound round =
                assessmentRoundMapper.toEntity(request);

        round.setPhase(phase);

        round.setIsActive(true);

        round.setCreatedAt(LocalDateTime.now());

        assessmentRoundRepository.save(round);

        return assessmentRoundMapper.toResponse(round);
    }


    @Override
    public AssessmentRoundResponse updateAssessmentRound(
            Long id,
            AssessmentRoundUpdateRequest request) {

        AssessmentRound round =
                assessmentRoundRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy đợt đánh giá với ID = "
                                                + id));

        LocalDate now = LocalDate.now();
        if (now.isBefore(round.getPhase().getStartDate()) || now.isAfter(round.getPhase().getEndDate())) {
            throw new BadRequestException(
                    "Không thể cập nhật AssessmentRound. Hiện tại không nằm trong thời gian của InternshipPhase ("
                            + round.getPhase().getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " đến " + round.getPhase().getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        }

        List<String> violations = new ArrayList<>();

        if (round.getRoundCriteria() != null
                && !round.getRoundCriteria().isEmpty()) {

            violations.add("RoundCriteria");
        }

        if (round.getAssessmentResults() != null
                && !round.getAssessmentResults().isEmpty()) {

            violations.add("AssessmentResult");
        }

        boolean isUsed = !violations.isEmpty();

        String linkedEntities =
                String.join(", ", violations);

        if (request.getRoundName() != null) {

            request.setRoundName(
                    request.getRoundName().trim());

            if (request.getRoundName().isBlank()) {

                throw new BadRequestException(
                        "Tên đợt đánh giá không được để trống");
            }

            if (!request.getRoundName()
                    .equalsIgnoreCase(round.getRoundName())) {

                if (isUsed) {

                    throw new ConflictException(
                            "Không thể thay đổi tên đợt đánh giá vì AssessmentRound đã liên kết với: "
                                    + linkedEntities);
                }

                if (assessmentRoundRepository
                        .existsByRoundNameIgnoreCaseAndPhase_PhaseId(
                                request.getRoundName(),
                                round.getPhase().getPhaseId())) {

                    throw new ConflictException(
                            "Tên đợt đánh giá : '"
                                    + request.getRoundName()
                                    + "' đã tồn tại trong Phase ID = "
                                    + round.getPhase().getPhaseId());
                }
            }
        }

        if (request.getDescription() != null) {

            request.setDescription(
                    request.getDescription().trim());
        }

        if (request.getStartDate() != null
                && !request.getStartDate()
                .equals(round.getStartDate())
                && isUsed) {

            throw new ConflictException(
                    "Không thể thay đổi ngày bắt đầu vì AssessmentRound đã liên kết với: "
                            + linkedEntities);
        }

        if (request.getEndDate() != null
                && !request.getEndDate()
                .equals(round.getEndDate())
                && isUsed) {

            throw new ConflictException(
                    "Không thể thay đổi ngày kết thúc vì AssessmentRound đã liên kết với: "
                            + linkedEntities);
        }

        LocalDate newStartDate =
                request.getStartDate() != null
                        ? request.getStartDate()
                        : round.getStartDate();

        LocalDate newEndDate =
                request.getEndDate() != null
                        ? request.getEndDate()
                        : round.getEndDate();

        if (newStartDate.isAfter(newEndDate)) {

            throw new BadRequestException(
                    "Ngày bắt đầu đợt đánh giá không được sau ngày kết thúc đợt đánh giá");
        }

        if (newStartDate.isBefore(round.getPhase().getStartDate())
                || newEndDate.isAfter(round.getPhase().getEndDate())) {

            throw new BadRequestException(
                    "Thời gian đợt đánh giá phải nằm trong khoảng thời gian của InternshipPhase ("
                            + round.getPhase().getStartDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " đến "
                            + round.getPhase().getEndDate()
                            .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + ")");
        }

        boolean hasChanges = false;

        if (request.getRoundName() != null
                && !request.getRoundName()
                .equalsIgnoreCase(round.getRoundName())) {

            hasChanges = true;
        }

        if (request.getStartDate() != null
                && !request.getStartDate()
                .equals(round.getStartDate())) {

            hasChanges = true;
        }

        if (request.getEndDate() != null
                && !request.getEndDate()
                .equals(round.getEndDate())) {

            hasChanges = true;
        }

        if (request.getDescription() != null
                && !request.getDescription()
                .equalsIgnoreCase(round.getDescription())) {

            hasChanges = true;
        }

        if (!hasChanges) {

            return null;
        }

        assessmentRoundMapper
                .updateEntityFromDto(request, round);

        round.setUpdatedAt(LocalDateTime.now());

        assessmentRoundRepository.save(round);

        return assessmentRoundMapper.toResponse(round);
    }

    @Override
    public AssessmentRoundResponse updateAssessmentRoundStatus(Long id, AssessmentRoundUpdateStatusRequest request) {

        AssessmentRound round = assessmentRoundRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đợt đánh giá với ID = " + id));

        boolean used =
                (round.getRoundCriteria() != null
                        && !round.getRoundCriteria().isEmpty())
                        || (round.getAssessmentResults() != null
                        && !round.getAssessmentResults().isEmpty());

        Boolean newActive =
                Boolean.parseBoolean(
                        request.getIsActive());

        if (!newActive.equals(round.getIsActive())
                && used) {

            throw new ConflictException(
                    "Đợt đánh giá đã được sử dụng, không thể đổi trạng thái hoạt động của đợt đánh giá");
        }

        if (newActive == round.getIsActive()) {
            return null;
        }

        round.setIsActive(newActive);

        round.setUpdatedAt(LocalDateTime.now());

        assessmentRoundRepository.save(round);

        return assessmentRoundMapper.toResponse(round);
    }

    @Override
    public void deleteAssessmentRound(
            Long id) {

        AssessmentRound round =
                assessmentRoundRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy đợt đánh giá với ID = "
                                                + id));

        LocalDate now = LocalDate.now();
        if (now.isBefore(round.getPhase().getStartDate()) || now.isAfter(round.getPhase().getEndDate())) {
            throw new BadRequestException(
                    "Không thể xóa AssessmentRound. Hiện tại không nằm trong thời gian của InternshipPhase ("
                            + round.getPhase().getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " đến " + round.getPhase().getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ")");
        }

        List<String> violations = new ArrayList<>();

        if (round.getRoundCriteria() != null
                && !round.getRoundCriteria().isEmpty()) {

            violations.add("RoundCriteria");
        }

        if (round.getAssessmentResults() != null
                && !round.getAssessmentResults().isEmpty()) {

            violations.add("AssessmentResult");
        }

        if (!violations.isEmpty()) {

            throw new ConflictException(
                    "Không thể xóa đợt đánh giá ID = "
                            + id
                            + " vì đã liên kết với: "
                            + String.join(", ", violations));
        }

        assessmentRoundRepository.delete(round);

    }
}
