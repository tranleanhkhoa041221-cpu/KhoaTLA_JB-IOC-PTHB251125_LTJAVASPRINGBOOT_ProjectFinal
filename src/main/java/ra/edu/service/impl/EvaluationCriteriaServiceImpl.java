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
import ra.edu.dto.request.EvaluationCriteriaCreateRequest;
import ra.edu.dto.request.EvaluationCriteriaFilterRequest;
import ra.edu.dto.request.EvaluationCriteriaUpdateRequest;
import ra.edu.dto.response.EvaluationCriteriaResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.EvaluationCriteria;
import ra.edu.entity.User;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.NotFoundException;
import ra.edu.helper.AccessValidator;
import ra.edu.mapper.EvaluationCriteriaMapper;
import ra.edu.repository.EvaluationCriteriaRepository;
import ra.edu.service.EvaluationCriteriaService;


import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    private final EvaluationCriteriaMapper evaluationCriteriaMapper;

    private final AccessValidator accessValidator;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<EvaluationCriteriaResponse> getAllCriteria(
            EvaluationCriteriaFilterRequest request) {

        accessValidator.validateAccess(getCurrentUser());

        if (request.getMinMaxScore() != null && request.getMaxMaxScore() != null
                && request.getMinMaxScore()
                .compareTo(request.getMaxMaxScore()) > 0) {

            throw new BadRequestException(
                    "minMaxScore không được lớn hơn maxMaxScore");
        }

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by("criterionId").descending());

        Page<EvaluationCriteria> criteriaPage;

        if (request.getCriterionName() != null
                && !request.getCriterionName().isBlank()) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByCriterionNameContainingIgnoreCase(
                                    request.getCriterionName(),
                                    pageable);

        } else if (request.getDescription() != null
                && !request.getDescription().isBlank()) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByDescriptionContainingIgnoreCase(
                                    request.getDescription(),
                                    pageable);

        } else if (request.getMaxScore() != null) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByMaxScore(
                                    request.getMaxScore(),
                                    pageable);

        } else if (request.getMinMaxScore() != null
                && request.getMaxMaxScore() != null) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByMaxScoreBetween(
                                    request.getMinMaxScore(),
                                    request.getMaxMaxScore(),
                                    pageable);

        } else if (request.getMinMaxScore() != null) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByMaxScoreGreaterThanEqual(
                                    request.getMinMaxScore(),
                                    pageable);

        } else if (request.getMaxMaxScore() != null) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByMaxScoreLessThanEqual(
                                    request.getMaxMaxScore(),
                                    pageable);

        } else {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAll(pageable);
        }

        List<EvaluationCriteriaResponse> items =
                criteriaPage.getContent()
                        .stream()
                        .map(evaluationCriteriaMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalPages(criteriaPage.getTotalPages())
                .totalItems(criteriaPage.getTotalElements())
                .build();

        return PaginationResponse
                .<EvaluationCriteriaResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }


    @Override
    public EvaluationCriteriaResponse getCriterionById(Long id) {

        accessValidator.validateAccess(getCurrentUser());

        EvaluationCriteria criteria = evaluationCriteriaRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy tiêu chí đánh giá với ID = " + id));

        return evaluationCriteriaMapper.toResponse(criteria);

    }

    @Override
    public EvaluationCriteriaResponse createCriterion(
            EvaluationCriteriaCreateRequest request) {

        if (evaluationCriteriaRepository
                .existsByCriterionNameIgnoreCase(
                        request.getCriterionName())) {

            throw new ConflictException(
                    "Tên tiêu chí đánh giá đã tồn tại");
        }

        EvaluationCriteria criteria = evaluationCriteriaMapper.toEntity(request);

        criteria.setCreatedAt(LocalDateTime.now());

        evaluationCriteriaRepository.save(criteria);

        return evaluationCriteriaMapper.toResponse(criteria);
    }

    @Override
    public EvaluationCriteriaResponse updateCriterion(
            Long id,
            EvaluationCriteriaUpdateRequest request) {

        EvaluationCriteria criteria =
                evaluationCriteriaRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí đánh giá với ID = "
                                                + id));

        boolean used =
                (criteria.getRoundCriteria() != null
                        && !criteria.getRoundCriteria().isEmpty())
                        || (criteria.getAssessmentResults() != null
                        && !criteria.getAssessmentResults().isEmpty());

        if (request.getCriterionName() != null) {

            request.setCriterionName(
                    request.getCriterionName().trim());

            if (request.getCriterionName().isBlank()) {

                throw new BadRequestException(
                        "Tên tiêu chí đánh giá không được để trống");
            }

            if (!request.getCriterionName()
                    .equalsIgnoreCase(criteria.getCriterionName())) {

                if (used) {

                    throw new ConflictException(
                            "Tiêu chí đánh giá đã được sử dụng, không thể thay đổi tên tiêu chí đánh giá");
                }
            }

            if (evaluationCriteriaRepository
                    .existsByCriterionNameIgnoreCase(
                            request.getCriterionName())) {

                throw new ConflictException(
                        "Tên tiêu chí đánh giá đã tồn tại");
            }
        }

        if (request.getDescription() != null) {
            request.setDescription(request.getDescription().trim());
        }

        if (request.getMaxScore() != null
                && request.getMaxScore()
                .compareTo(criteria.getMaxScore()) != 0
                && used) {

            throw new ConflictException(
                    "Tiêu chí đánh giá đã được sử dụng, không thể thay đổi điểm tối đa");
        }

        evaluationCriteriaMapper.updateEntityFromDto(request, criteria);

        criteria.setUpdatedAt(LocalDateTime.now());

        evaluationCriteriaRepository.save(criteria);

        return evaluationCriteriaMapper.toResponse(criteria);
    }


    @Override
    public EvaluationCriteriaResponse deleteCriterion(Long id) {

        EvaluationCriteria criteria =
                evaluationCriteriaRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí đánh giá với ID = "
                                                + id));

        boolean hasRoundCriteria =
                criteria.getRoundCriteria() != null
                        && !criteria.getRoundCriteria().isEmpty();

        boolean hasAssessmentResult =
                criteria.getAssessmentResults() != null
                        && !criteria.getAssessmentResults().isEmpty();

        if (hasRoundCriteria && hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể xóa tiêu chí đánh giá ID = "
                            + id
                            + " vì đã liên kết với RoundCriteria và AssessmentResult");
        }

        if (hasRoundCriteria) {

            throw new ConflictException(
                    "Không thể xóa tiêu chí đánh giá ID = "
                            + id
                            + " vì đã liên kết với RoundCriteria");
        }

        if (hasAssessmentResult) {

            throw new ConflictException(
                    "Không thể xóa tiêu chí đánh giá ID = "
                            + id
                            + " vì đã liên kết với AssessmentResult");
        }

        EvaluationCriteriaResponse response =
                evaluationCriteriaMapper.toResponse(criteria);

        evaluationCriteriaRepository.delete(criteria);

        return response;
    }

}
