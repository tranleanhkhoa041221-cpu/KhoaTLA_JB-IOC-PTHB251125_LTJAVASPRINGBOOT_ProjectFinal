package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.AssessmentRound;

import java.time.LocalDate;

public interface AssessmentRoundRepository extends JpaRepository<AssessmentRound, Long> {

    boolean existsByRoundNameIgnoreCaseAndPhase_PhaseId(String roundName, Long phaseId);

    Page<AssessmentRound> findAllByRoundNameContainingIgnoreCase
            (String roundName, Pageable pageable);

    Page<AssessmentRound> findAllByStartDate(LocalDate startDate, Pageable pageable);

    Page<AssessmentRound> findAllByEndDate(LocalDate endDate, Pageable pageable);

    Page<AssessmentRound> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual
            (LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<AssessmentRound> findAllByDescriptionContainingIgnoreCase
            (String description, Pageable pageable);

    Page<AssessmentRound> findAllByPhase_PhaseId(Long phaseId, Pageable pageable);

    Page<AssessmentRound> findAllByPhase_PhaseNameContainingIgnoreCase
            (String phaseName, Pageable pageable);

    Page<AssessmentRound> findAllByIsActive(Boolean isActive, Pageable pageable);

}
