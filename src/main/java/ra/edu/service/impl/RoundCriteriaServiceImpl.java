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
import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaFilterRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.RoundCriteriaResponse;
import ra.edu.entity.AssessmentRound;
import ra.edu.entity.EvaluationCriteria;
import ra.edu.entity.RoundCriteria;
import ra.edu.entity.User;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
import ra.edu.mapper.RoundCriteriaMapper;
import ra.edu.repository.*;
import ra.edu.service.RoundCriteriaService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundCriteriaServiceImpl implements RoundCriteriaService {

    private final RoundCriteriaRepository roundCriteriaRepository;

    private final AssessmentRoundRepository assessmentRoundRepository;

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    private final RoundCriteriaMapper roundCriteriaMapper;

    private final StudentRepository studentRepository;

    private final MentorRepository mentorRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<RoundCriteriaResponse> getAllRoundCriteria(
            RoundCriteriaFilterRequest request) {

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
                    "Không có quyền truy cập RoundCriteria");
        }

        if (request.getMinWeight() != null
                && request.getMaxWeight() != null
                && request.getMinWeight()
                .compareTo(request.getMaxWeight()) > 0) {

            throw new BadRequestException(
                    "minWeight không được lớn hơn maxWeight");
        }

        if (request.getRoundId() != null
                && !assessmentRoundRepository
                .existsById(request.getRoundId())) {

            throw new NotFoundException(
                    "Không tìm thấy đợt đánh giá với ID = "
                            + request.getRoundId());
        }

        if (request.getCriterionId() != null
                && !evaluationCriteriaRepository
                .existsById(request.getCriterionId())) {

            throw new NotFoundException(
                    "Không tìm thấy tiêu chí đánh giá với ID = "
                            + request.getCriterionId());
        }

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by("roundCriterionId").descending());

        Page<RoundCriteria> roundCriteriaPage;

        if (request.getRoundId() != null
                && request.getCriterionId() != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByRound_RoundIdAndCriterion_CriterionId(
                                    request.getRoundId(),
                                    request.getCriterionId(),
                                    pageable);

        } else if (request.getRoundId() != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByRound_RoundId(
                                    request.getRoundId(),
                                    pageable);

        } else if (request.getCriterionId() != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByCriterion_CriterionId(
                                    request.getCriterionId(),
                                    pageable);

        } else if (request.getRoundName() != null
                && !request.getRoundName().isBlank()) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByRound_RoundNameContainingIgnoreCase(
                                    request.getRoundName(),
                                    pageable);

        } else if (request.getCriterionName() != null
                && !request.getCriterionName().isBlank()) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByCriterion_CriterionNameContainingIgnoreCase(
                                    request.getCriterionName(),
                                    pageable);

        } else if (request.getWeight() != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeight(
                                    request.getWeight(),
                                    pageable);

        } else if (request.getMinWeight() != null
                && request.getMaxWeight() != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeightBetween(
                                    request.getMinWeight(),
                                    request.getMaxWeight(),
                                    pageable);

        } else if (request.getMinWeight() != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeightGreaterThanEqual(
                                    request.getMinWeight(),
                                    pageable);

        } else if (request.getMaxWeight() != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeightLessThanEqual(
                                    request.getMaxWeight(),
                                    pageable);

        } else {

            roundCriteriaPage =
                    roundCriteriaRepository.findAll(pageable);
        }

        List<RoundCriteriaResponse> items =
                roundCriteriaPage.getContent()
                        .stream()
                        .map(roundCriteriaMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalPages(roundCriteriaPage.getTotalPages())
                .totalItems(roundCriteriaPage.getTotalElements())
                .build();

        return PaginationResponse
                .<RoundCriteriaResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public RoundCriteriaResponse getRoundCriteriaById(Long id) {

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {

                RoundCriteria roundCriteria =
                        roundCriteriaRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                        + id));

                return roundCriteriaMapper.toResponse(roundCriteria);
            }

            case MENTOR -> {

                mentorRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role MENTOR"));

                RoundCriteria roundCriteria =
                        roundCriteriaRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                        + id));

                return roundCriteriaMapper.toResponse(roundCriteria);
            }

            case STUDENT -> {

                studentRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role STUDENT"));

                RoundCriteria roundCriteria =
                        roundCriteriaRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                        + id));

                return roundCriteriaMapper.toResponse(roundCriteria);
            }

            default -> throw new ForbiddenException(
                    "Không có quyền truy cập RoundCriteria");
        }
    }

    @Override
    public RoundCriteriaResponse createRoundCriteria(
            RoundCriteriaCreateRequest request) {

        AssessmentRound round =
                assessmentRoundRepository.findById(request.getRoundId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy đợt đánh giá với ID = "
                                                + request.getRoundId()));

        EvaluationCriteria criterion =
                evaluationCriteriaRepository.findById(
                                request.getCriterionId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí đánh giá với ID = "
                                                + request.getCriterionId()));

        if (roundCriteriaRepository
                .existsByRound_RoundIdAndCriterion_CriterionId(
                        request.getRoundId(),
                        request.getCriterionId())) {

            throw new ConflictException(
                    "Tiêu chí đánh giá ID = "
                            + request.getCriterionId()
                            + " đã tồn tại trong đợt đánh giá ID = "
                            + request.getRoundId());
        }

        RoundCriteria roundCriteria = roundCriteriaMapper.toEntity(request);

        roundCriteria.setRound(round);

        roundCriteria.setCriterion(criterion);

        roundCriteria.setCreatedAt(LocalDateTime.now());

        roundCriteria.setUpdatedAt(LocalDateTime.now());

        roundCriteriaRepository.save(roundCriteria);

        return roundCriteriaMapper.toResponse(roundCriteria);
    }

    @Override
    public RoundCriteriaResponse updateRoundCriteria(
            Long id,
            RoundCriteriaUpdateRequest request) {

        RoundCriteria roundCriteria =
                roundCriteriaRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                + id));

        roundCriteriaMapper.updateEntityFromDto(request, roundCriteria);

        roundCriteria.setUpdatedAt(LocalDateTime.now());

        roundCriteriaRepository.save(roundCriteria);

        return roundCriteriaMapper.toResponse(roundCriteria);
    }


    @Override
    public RoundCriteriaResponse deleteRoundCriteria(Long id) {

        RoundCriteria roundCriteria =
                roundCriteriaRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                + id));

        RoundCriteriaResponse response =
                roundCriteriaMapper.toResponse(roundCriteria);

        roundCriteriaRepository.delete(roundCriteria);

        return response;
    }

}
