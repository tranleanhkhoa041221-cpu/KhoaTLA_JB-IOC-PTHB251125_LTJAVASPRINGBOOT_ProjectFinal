package ra.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.AssessmentResult;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

    boolean existsByEvaluatedBy_UserId(Long userId);

}
