package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.RoundCriteriaResponse;
import ra.edu.entity.AssessmentRound;
import ra.edu.entity.EvaluationCriteria;
import ra.edu.entity.RoundCriteria;
import ra.edu.mapper.RoundCriteriaMapper;
import ra.edu.repository.AssessmentRoundRepository;
import ra.edu.repository.EvaluationCriteriaRepository;
import ra.edu.repository.RoundCriteriaRepository;
import ra.edu.service.RoundCriteriaService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundCriteriaServiceImpl implements RoundCriteriaService {

    private final RoundCriteriaRepository roundCriteriaRepository;

    private final AssessmentRoundRepository assessmentRoundRepository;

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    private final RoundCriteriaMapper roundCriteriaMapper;

    @Override
    public PaginationResponse<RoundCriteriaResponse> getAllRoundCriteria(
            int page,
            int size,
            Long roundId,
            Long criterionId,
            String roundName,
            String criterionName,
            BigDecimal weight,
            BigDecimal minWeight,
            BigDecimal maxWeight) {


        if (minWeight != null && maxWeight != null
                && minWeight.compareTo(maxWeight) > 0) {

            throw new IllegalArgumentException(
                    "minWeight không được lớn hơn maxWeight");
        }

        if (roundId != null && !assessmentRoundRepository.existsById(roundId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy đợt đánh giá với ID = " + roundId);
        }

        if (criterionId != null
                && !evaluationCriteriaRepository.existsById(criterionId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy tiêu chí đánh giá với ID = " + criterionId);
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("roundCriterionId").descending());

        Page<RoundCriteria> roundCriteriaPage;

        if (roundId != null && criterionId != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByRound_RoundIdAndCriterion_CriterionId(
                                    roundId, criterionId, pageable);

        } else if (roundId != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByRound_RoundId(roundId, pageable);

        } else if (criterionId != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByCriterion_CriterionId(criterionId, pageable);

        } else if (roundName != null && !roundName.isBlank()) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByRound_RoundNameContainingIgnoreCase(roundName, pageable);

        } else if (criterionName != null && !criterionName.isBlank()) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByCriterion_CriterionNameContainingIgnoreCase(criterionName, pageable);

        } else if (weight != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeight(weight, pageable);

        } else if (minWeight != null && maxWeight != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeightBetween(minWeight, maxWeight, pageable);

        } else if (minWeight != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeightGreaterThanEqual(minWeight, pageable);

        } else if (maxWeight != null) {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAllByWeightLessThanEqual(maxWeight, pageable);

        } else {

            roundCriteriaPage =
                    roundCriteriaRepository
                            .findAll(pageable);
        }

        List<RoundCriteriaResponse> items =
                roundCriteriaPage.getContent()
                        .stream()
                        .map(roundCriteriaMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
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

        RoundCriteria roundCriteria =
                roundCriteriaRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                + id
                                )
                        );

        return roundCriteriaMapper.toResponse(roundCriteria);
    }

    @Override
    public RoundCriteriaResponse createRoundCriteria(
            RoundCriteriaCreateRequest request) {

        AssessmentRound round =
                assessmentRoundRepository.findById(request.getRoundId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy đợt đánh giá với ID = "
                                                + request.getRoundId()));

        EvaluationCriteria criterion =
                evaluationCriteriaRepository.findById(
                                request.getCriterionId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy tiêu chí đánh giá với ID = "
                                                + request.getCriterionId()));

        if (roundCriteriaRepository
                .existsByRound_RoundIdAndCriterion_CriterionId(
                        request.getRoundId(),
                        request.getCriterionId())) {

            throw new IllegalStateException(
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
                                new EntityNotFoundException(
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
                                new EntityNotFoundException(
                                        "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                + id));

        RoundCriteriaResponse response = roundCriteriaMapper
                .toResponse(roundCriteria);

        roundCriteriaRepository.delete(roundCriteria);

        return response;
    }

}
