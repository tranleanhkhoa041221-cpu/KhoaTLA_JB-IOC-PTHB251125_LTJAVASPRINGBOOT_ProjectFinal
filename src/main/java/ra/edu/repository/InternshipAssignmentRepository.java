package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.InternshipAssignment;

public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Long> {



}
