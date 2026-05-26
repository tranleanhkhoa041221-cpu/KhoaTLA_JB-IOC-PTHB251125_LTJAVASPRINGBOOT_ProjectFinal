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
import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseFilterRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.InternshipPhase;
import ra.edu.entity.User;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.NotFoundException;
import ra.edu.helper.AccessValidator;
import ra.edu.mapper.InternshipPhaseMapper;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.service.InternshipPhaseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    private final InternshipPhaseMapper internshipPhaseMapper;

    private final AccessValidator accessValidator;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<InternshipPhaseResponse> getAllPhases(
            InternshipPhaseFilterRequest request) {

        accessValidator.validateAccess(getCurrentUser());

        if (request.getStartDate() != null
                && request.getEndDate() != null
                && request.getStartDate().isAfter(request.getEndDate())) {

            throw new BadRequestException(
                    "Ngày bắt đầu giai đoạn thực tập không được sau ngày kết thúc giai đoạn thực tập");
        }

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by("phaseId").descending()
        );

        Page<InternshipPhase> pageResult;

        if (request.getPhaseName() != null && !request.getPhaseName().isBlank()) {

            pageResult = internshipPhaseRepository
                    .findAllByPhaseNameContainingIgnoreCase(
                            request.getPhaseName(), pageable);

        } else if (request.getStartDate() != null && request.getEndDate() != null) {

            pageResult = internshipPhaseRepository
                    .findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(
                            request.getStartDate(),
                            request.getEndDate(),
                            pageable);

        } else if (request.getStartDate() != null) {

            pageResult = internshipPhaseRepository
                    .findAllByStartDate(request.getStartDate(), pageable);

        } else if (request.getEndDate() != null) {

            pageResult = internshipPhaseRepository
                    .findAllByEndDate(request.getEndDate(), pageable);

        } else if (request.getDescription() != null && !request.getDescription().isBlank()) {

            pageResult = internshipPhaseRepository
                    .findAllByDescriptionContainingIgnoreCase(
                            request.getDescription(), pageable);

        } else {

            pageResult = internshipPhaseRepository.findAll(pageable);
        }

        List<InternshipPhaseResponse> items =
                pageResult.getContent()
                        .stream()
                        .map(internshipPhaseMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalPages(pageResult.getTotalPages())
                .totalItems(pageResult.getTotalElements())
                .build();

        return PaginationResponse.<InternshipPhaseResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public InternshipPhaseResponse getPhaseById(Long id) {

        accessValidator.validateAccess(getCurrentUser());

        InternshipPhase phase = internshipPhaseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy Phase với ID = " + id));

        return internshipPhaseMapper.toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse createPhase(
            InternshipPhaseCreateRequest request) {

        if (internshipPhaseRepository
                .existsByPhaseNameIgnoreCase(request.getPhaseName())) {

            throw new ConflictException("Tên phase đã tồn tại");
        }

        if (request.getStartDate().isAfter(request.getEndDate())) {

            throw new BadRequestException(
                    "Ngày bắt đầu giai đoạn thực tập không được sau ngày kết thúc giai đoạn thực tập");

        }

        InternshipPhase phase = internshipPhaseMapper.toEntity(request);

        phase.setCreatedAt(LocalDateTime.now());

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
                                new NotFoundException(
                                        "Không tìm thấy Phase với ID = " + id));

        List<String> violations = new ArrayList<>();
        if (phase.getInternshipAssignments() != null && !phase.getInternshipAssignments().isEmpty()) {
            violations.add("InternshipAssignment");
        }
        if (phase.getAssessmentRounds() != null && !phase.getAssessmentRounds().isEmpty()) {
            violations.add("AssessmentRound");
        }

        boolean isUsed = !violations.isEmpty();
        String linkedEntities = String.join(", ", violations);

        if (request.getPhaseName() != null) {
            request.setPhaseName(request.getPhaseName().trim());

            if (request.getPhaseName().isBlank()) {
                throw new BadRequestException("Tên giai đoạn thực tập không được để trống");
            }

            if (!request.getPhaseName().equalsIgnoreCase(phase.getPhaseName())) {
                if (isUsed) {
                    throw new ConflictException("Không thể thay đổi tên giai đoạn vì Phase đã liên kết với: " + linkedEntities);
                }
                if (internshipPhaseRepository.existsByPhaseNameIgnoreCase(request.getPhaseName())) {
                    throw new ConflictException("Tên giai đoạn thực tập đã tồn tại");
                }
            }
        }

        if (request.getDescription() != null) {
            request.setDescription(request.getDescription().trim());
        }

        if (request.getStartDate() != null && !request.getStartDate().equals(phase.getStartDate()) && isUsed) {
            throw new ConflictException("Không thể thay đổi StartDate vì Phase đã liên kết với: " + linkedEntities);
        }

        if (request.getEndDate() != null && !request.getEndDate().equals(phase.getEndDate()) && isUsed) {
            throw new ConflictException("Không thể thay đổi EndDate vì Phase đã liên kết với: " + linkedEntities);
        }

        LocalDate newStartDate = request.getStartDate() != null ? request.getStartDate() : phase.getStartDate();
        LocalDate newEndDate = request.getEndDate() != null ? request.getEndDate() : phase.getEndDate();

        if (newStartDate.isAfter(newEndDate)) {
            throw new BadRequestException("Ngày bắt đầu giai đoạn thực tập không được sau ngày kết thúc giai đoạn thực tập");
        }

        boolean hasChanges = false;

        if (request.getPhaseName() != null && !request.getPhaseName().equalsIgnoreCase(phase.getPhaseName())) {
            hasChanges = true;
        }
        if (request.getStartDate() != null && !request.getStartDate().equals(phase.getStartDate())) {
            hasChanges = true;
        }
        if (request.getEndDate() != null && !request.getEndDate().equals(phase.getEndDate())) {
            hasChanges = true;
        }
        if (request.getDescription() != null && !request.getDescription().equalsIgnoreCase(phase.getDescription())) {
            hasChanges = true;
        }

        if (!hasChanges) {
            return null;
        }

        internshipPhaseMapper.updateEntityFromDto(request, phase);

        phase.setUpdatedAt(LocalDateTime.now());

        internshipPhaseRepository.save(phase);

        return internshipPhaseMapper.toResponse(phase);
    }

    @Override
    public void deletePhase(Long id) {

        InternshipPhase phase =
                internshipPhaseRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy Phase với ID = " + id));

        List<String> violations = new ArrayList<>();

        if (phase.getInternshipAssignments() != null && !phase.getInternshipAssignments().isEmpty()) {
            violations.add("InternshipAssignment");
        }
        if (phase.getAssessmentRounds() != null && !phase.getAssessmentRounds().isEmpty()) {
            violations.add("AssessmentRound");
        }

        if (!violations.isEmpty()) {
            throw new ConflictException("Không thể xóa Phase ID = " + id
                    + " vì đã liên kết với: " + String.join(", ", violations));
        }

        internshipPhaseRepository.delete(phase);

    }


}
