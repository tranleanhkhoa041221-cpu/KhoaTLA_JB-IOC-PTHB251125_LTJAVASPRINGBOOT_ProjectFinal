package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.EvaluationCriteriaCreateRequest;
import ra.edu.dto.request.EvaluationCriteriaUpdateRequest;
import ra.edu.dto.response.EvaluationCriteriaResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.EvaluationCriteria;
import ra.edu.mapper.EvaluationCriteriaMapper;
import ra.edu.repository.EvaluationCriteriaRepository;
import ra.edu.service.EvaluationCriteriaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationCriteriaServiceImpl implements EvaluationCriteriaService {

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    private final EvaluationCriteriaMapper evaluationCriteriaMapper;

    @Override
    public PaginationResponse<EvaluationCriteriaResponse> getAllCriteria(
            int page,
            int size,
            String criterionName,
            String description,
            BigDecimal maxScore,
            BigDecimal minMaxScore,
            BigDecimal maxMaxScore) {

        if (minMaxScore != null && maxMaxScore != null
                && minMaxScore.compareTo(maxMaxScore) > 0) {
            throw new IllegalArgumentException("minMaxScore không được lớn hơn maxMaxScore");

        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("criterionId").descending());

        Page<EvaluationCriteria> criteriaPage;

        if (criterionName != null && !criterionName.isBlank()) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByCriterionNameContainingIgnoreCase
                                    (criterionName, pageable);

        } else if (description != null && !description.isBlank()) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByDescriptionContainingIgnoreCase
                                    (description, pageable);

        } else if (maxScore != null) {

            criteriaPage =
                    evaluationCriteriaRepository
                            .findAllByMaxScore(maxScore, pageable);

        } else if (minMaxScore != null && maxMaxScore != null) {

            criteriaPage = evaluationCriteriaRepository
                    .findAllByMaxScoreBetween(minMaxScore, maxMaxScore, pageable);

        } else if (minMaxScore != null) {

            criteriaPage = evaluationCriteriaRepository
                    .findAllByMaxScoreGreaterThanEqual(minMaxScore, pageable);

        } else if (maxMaxScore != null) {
            criteriaPage = evaluationCriteriaRepository
                    .findAllByMaxScoreLessThanEqual(maxMaxScore, pageable);

        } else {

            criteriaPage = evaluationCriteriaRepository
                    .findAll(pageable);
        }

        List<EvaluationCriteriaResponse> items = criteriaPage.getContent()
                .stream()
                .map(evaluationCriteriaMapper::toResponse)
                .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(criteriaPage.getTotalPages())
                .totalItems(criteriaPage.getTotalElements())
                .build();

        return PaginationResponse.<EvaluationCriteriaResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public EvaluationCriteriaResponse getCriterionById(Long id) {

        EvaluationCriteria criteria = evaluationCriteriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tiêu chí đánh giá với ID = " + id));

        return evaluationCriteriaMapper.toResponse(criteria);
    }

    @Override
    public EvaluationCriteriaResponse createCriterion(EvaluationCriteriaCreateRequest request) {

        if (evaluationCriteriaRepository.existsByCriterionNameIgnoreCase(request.getCriterionName())) {

            throw new IllegalStateException("Tên tiêu chí đánh giá đã tồn tại");
        }

        EvaluationCriteria criteria = evaluationCriteriaMapper.toEntity(request);

        criteria.setCreatedAt(LocalDateTime.now());
        criteria.setUpdatedAt(LocalDateTime.now());

        evaluationCriteriaRepository.save(criteria);

        return evaluationCriteriaMapper.toResponse(criteria);
    }

    @Override
    public EvaluationCriteriaResponse updateCriterion(Long id, EvaluationCriteriaUpdateRequest request) {

        EvaluationCriteria criteria = evaluationCriteriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tiêu chí đánh giá với ID = " + id));

        boolean used =
                (criteria.getRoundCriteria() != null
                        && !criteria.getRoundCriteria().isEmpty())
                        || (criteria.getAssessmentResults() != null
                        && !criteria.getAssessmentResults().isEmpty());

        if (request.getCriterionName() != null
                && !request.getCriterionName().equalsIgnoreCase(criteria.getCriterionName())) {

            if (used) {

                throw new IllegalStateException("Tiêu chí đánh giá đã được sử dụng, không thể thay đổi tên tiêu chí đánh giá");
            }

            if (evaluationCriteriaRepository.existsByCriterionNameIgnoreCase(request.getCriterionName())) {

                throw new IllegalStateException("Tên tiêu chí đánh giá đã tồn tại");
            }
        }

        if (request.getMaxScore() != null
                && request.getMaxScore().compareTo(criteria.getMaxScore()) != 0 && used) {

            throw new IllegalStateException("Tiêu chí đánh giá đã được sử dụng, không thể thay đổi điểm tối đa");
        }

        evaluationCriteriaMapper.updateEntityFromDto(request, criteria);

        criteria.setUpdatedAt(LocalDateTime.now());

        evaluationCriteriaRepository.save(criteria);

        return evaluationCriteriaMapper.toResponse(criteria);
    }

    @Override
    public EvaluationCriteriaResponse deleteCriterion(Long id) {

        EvaluationCriteria criteria = evaluationCriteriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Không tìm thấy tiêu chí đánh giá với ID = " + id));

        if (criteria.getRoundCriteria() != null
                && !criteria.getRoundCriteria().isEmpty()) {

            throw new IllegalStateException("Không thể xóa tiêu chí đánh giá ID = " + id + " vì đã liên kết với RoundCriteria");
        }

        if (criteria.getAssessmentResults() != null
                && !criteria.getAssessmentResults().isEmpty()) {

            throw new IllegalStateException("Không thể xóa tiêu chí đánh giá ID = " + id + " vì đã liên kết với AssessmentResult");
        }

        EvaluationCriteriaResponse response =
                evaluationCriteriaMapper.toResponse(criteria);

        evaluationCriteriaRepository.delete(criteria);

        return response;
    }

}
