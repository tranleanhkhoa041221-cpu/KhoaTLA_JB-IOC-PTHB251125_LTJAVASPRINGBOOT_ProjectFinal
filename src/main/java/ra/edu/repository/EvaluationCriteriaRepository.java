package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.EvaluationCriteria;

import java.math.BigDecimal;

public interface EvaluationCriteriaRepository extends JpaRepository<EvaluationCriteria, Long> {

    boolean existsByCriterionNameIgnoreCase(String criterionName);

    Page<EvaluationCriteria> findAllByCriterionNameContainingIgnoreCase
            (String criterionName, Pageable pageable);

    Page<EvaluationCriteria> findAllByDescriptionContainingIgnoreCase
            (String description, Pageable pageable);

    Page<EvaluationCriteria> findAllByMaxScore
            (BigDecimal maxScore, Pageable pageable);

    Page<EvaluationCriteria> findAllByMaxScoreGreaterThanEqual
            (BigDecimal minScore, Pageable pageable);

    Page<EvaluationCriteria> findAllByMaxScoreLessThanEqual
            (BigDecimal maxScore, Pageable pageable);

    Page<EvaluationCriteria> findAllByMaxScoreBetween(
            BigDecimal minScore,
            BigDecimal maxScore,
            Pageable pageable);

}
