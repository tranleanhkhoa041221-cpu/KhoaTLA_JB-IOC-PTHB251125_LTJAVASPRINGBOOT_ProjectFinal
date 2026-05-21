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
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
import ra.edu.mapper.InternshipPhaseMapper;
import ra.edu.repository.InternshipPhaseRepository;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;
import ra.edu.service.InternshipPhaseService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InternshipPhaseServiceImpl implements InternshipPhaseService {

    private final InternshipPhaseRepository internshipPhaseRepository;

    private final InternshipPhaseMapper internshipPhaseMapper;

    private final MentorRepository mentorRepository;

    private final StudentRepository studentRepository;

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

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {
            }

            case MENTOR -> mentorRepository
                    .findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "User ID = "
                                            + currentUser.getUserId()
                                            + " chưa được liên kết với role MENTOR"));

            case STUDENT -> studentRepository
                    .findByUser_UserId(currentUser.getUserId())
                    .orElseThrow(() ->
                            new NotFoundException(
                                    "User ID = "
                                            + currentUser.getUserId()
                                            + " chưa được liên kết với role STUDENT"));

            default -> throw new ForbiddenException(
                    "Không có quyền truy cập InternshipPhase");
        }

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

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {

                InternshipPhase phase =
                        internshipPhaseRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy Phase với ID = " + id));

                return internshipPhaseMapper.toResponse(phase);
            }

            case MENTOR -> {

                mentorRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role MENTOR"));

                InternshipPhase phase =
                        internshipPhaseRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy Phase với ID = " + id));

                return internshipPhaseMapper.toResponse(phase);
            }

            case STUDENT -> {

                studentRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role STUDENT"));

                InternshipPhase phase =
                        internshipPhaseRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy Phase với ID = " + id));

                return internshipPhaseMapper.toResponse(phase);
            }

            default -> throw new ForbiddenException(
                    "Không có quyền truy cập InternshipPhase");
        }
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
                                new NotFoundException(
                                        "Không tìm thấy Phase với ID = " + id));

        boolean used =
                (phase.getInternshipAssignments() != null
                        && !phase.getInternshipAssignments().isEmpty())
                        || (phase.getAssessmentRounds() != null
                        && !phase.getAssessmentRounds().isEmpty());


        if (request.getPhaseName() != null
                && !request.getPhaseName().equalsIgnoreCase(phase.getPhaseName())) {

            if (used) {

                throw new ConflictException(
                        "Phase đã được sử dụng, không thể thay đổi tên giai đoạn thực tập");
            }

            if (internshipPhaseRepository.existsByPhaseNameIgnoreCase(
                    request.getPhaseName())) {

                throw new ConflictException(
                        "Tên giai đoạn thực tập đã tồn tại");
            }
        }

        if (request.getStartDate() != null && !request.getStartDate()
                .equals(phase.getStartDate()) && used) {

            throw new ConflictException(
                    "Phase đã được sử dụng, không thể thay đổi ngày bắt đầu giai đoạn thực tập");
        }

        if (request.getEndDate() != null && !request.getEndDate()
                .equals(phase.getEndDate()) && used) {

            throw new ConflictException(
                    "Phase đã được sử dụng, không thể thay đổi ngày kết thúc giai đoạn thực tập");
        }

        LocalDate newStartDate = request.getStartDate() != null
                ? request.getStartDate()
                : phase.getStartDate();

        LocalDate newEndDate = request.getEndDate() != null
                ? request.getEndDate()
                : phase.getEndDate();

        if (newStartDate.isAfter(newEndDate)) {

            throw new BadRequestException(
                    "Ngày bắt đầu giai đoạn thực tập không được sau ngày kết thúc giai đoạn thực tập");
        }

        internshipPhaseMapper.updateEntityFromDto(request, phase);

        phase.setUpdatedAt(LocalDateTime.now());

        internshipPhaseRepository.save(phase);

        return internshipPhaseMapper.toResponse(phase);
    }

    @Override
    public InternshipPhaseResponse deletePhase(Long id) {

        InternshipPhase phase =
                internshipPhaseRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy Phase với ID = " + id));

        boolean hasAssignments = phase.getInternshipAssignments() != null &&
                !phase.getInternshipAssignments().isEmpty();

        boolean hasRounds =  phase.getAssessmentRounds() != null &&
                !phase.getAssessmentRounds().isEmpty();

        if (hasAssignments && hasRounds) {

            throw new ConflictException(
                    "Không thể xóa Phase ID = "
                            + id
                            + " vì đã liên kết với InternshipAssignment và AssessmentRound");
        }

        if (hasAssignments) {

            throw new ConflictException(
                    "Không thể xóa Phase ID = "
                            + id
                            + " vì đã liên kết với InternshipAssignment");
        }

        if (hasRounds) {

            throw new ConflictException(
                    "Không thể xóa Phase ID = "
                            + id
                            + " vì đã liên kết với AssessmentRound");
        }

        InternshipPhaseResponse response =
                internshipPhaseMapper.toResponse(phase);

        internshipPhaseRepository.delete(phase);

        return response;
    }


}
