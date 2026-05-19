package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.RoundCriteria;

import java.math.BigDecimal;

public interface RoundCriteriaRepository extends JpaRepository<RoundCriteria, Long> {

    boolean existsByRound_RoundIdAndCriterion_CriterionId(Long roundId, Long criterionId);

    Page<RoundCriteria> findAllByRound_RoundIdAndCriterion_CriterionId(Long roundId, Long criterionId, Pageable pageable);

    Page<RoundCriteria> findAllByRound_RoundId(Long roundId, Pageable pageable);

    Page<RoundCriteria> findAllByCriterion_CriterionId(Long criterionId, Pageable pageable);

    Page<RoundCriteria> findAllByRound_RoundNameContainingIgnoreCase(String roundName, Pageable pageable);

    Page<RoundCriteria> findAllByCriterion_CriterionNameContainingIgnoreCase(String criterionName, Pageable pageable);

    Page<RoundCriteria> findAllByWeight(BigDecimal weight, Pageable pageable);

    Page<RoundCriteria> findAllByWeightGreaterThanEqual(BigDecimal minWeight, Pageable pageable);

    Page<RoundCriteria> findAllByWeightLessThanEqual(BigDecimal maxWeight, Pageable pageable);

    Page<RoundCriteria> findAllByWeightBetween(BigDecimal minWeight, BigDecimal maxWeight, Pageable pageable);


}
