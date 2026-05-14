package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.InternshipPhase;

import java.time.LocalDate;

public interface InternshipPhaseRepository extends JpaRepository<InternshipPhase, Long> {

    boolean existsByPhaseName(String phaseName);

    Page<InternshipPhase> findAllByPhaseNameContainingIgnoreCase(String phaseName, Pageable pageable);

    Page<InternshipPhase> findAllByStartDate(LocalDate startDate, Pageable pageable);

    Page<InternshipPhase> findAllByEndDate(LocalDate endDate, Pageable pageable);

    Page<InternshipPhase> findAllByStartDateGreaterThanEqualAndEndDateLessThanEqual(
            LocalDate startDate, LocalDate endDate, Pageable pageable);

    Page<InternshipPhase> findAllByDescriptionContainingIgnoreCase(String description, Pageable pageable);
}
