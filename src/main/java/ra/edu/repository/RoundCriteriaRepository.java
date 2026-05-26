package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ra.edu.entity.RoundCriteria;

import java.math.BigDecimal;
import java.util.List;

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

    @Query("""
            SELECT COALESCE(SUM(rc.weight), 0)
            FROM RoundCriteria rc
            WHERE rc.round.roundId = :roundId
            """)
    BigDecimal sumWeightByRound_RoundId(
            @Param("roundId") Long roundId);

    Page<RoundCriteria>
    findAllByRound_RoundIdAndCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            Long roundId,
            Long criterionId,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            Long roundId,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            Long criterionId,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByRound_RoundNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            String roundName,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByCriterion_CriterionNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            String criterionName,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            BigDecimal weight,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightBetweenAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            BigDecimal minWeight,
            BigDecimal maxWeight,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightGreaterThanEqualAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            BigDecimal minWeight,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightLessThanEqualAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            BigDecimal maxWeight,
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByRound_Phase_InternshipAssignments_Mentor_MentorId(
            Long mentorId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByRound_RoundIdAndCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Student_StudentId(
            Long roundId,
            Long criterionId,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Student_StudentId(
            Long roundId,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Student_StudentId(
            Long criterionId,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByRound_RoundNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Student_StudentId(
            String roundName,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByCriterion_CriterionNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Student_StudentId(
            String criterionName,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightAndRound_Phase_InternshipAssignments_Student_StudentId(
            BigDecimal weight,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightBetweenAndRound_Phase_InternshipAssignments_Student_StudentId(
            BigDecimal minWeight,
            BigDecimal maxWeight,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightGreaterThanEqualAndRound_Phase_InternshipAssignments_Student_StudentId(
            BigDecimal minWeight,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByWeightLessThanEqualAndRound_Phase_InternshipAssignments_Student_StudentId(
            BigDecimal maxWeight,
            Long studentId,
            Pageable pageable);

    Page<RoundCriteria>
    findAllByRound_Phase_InternshipAssignments_Student_StudentId(
            Long studentId,
            Pageable pageable);

    List<RoundCriteria>
    findAllByRound_RoundId(
            Long roundId,
            Sort sort);

    List<RoundCriteria>
    findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            Long roundId,
            Long mentorId,
            Sort sort);

    List<RoundCriteria>
    findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Student_StudentId(
            Long roundId,
            Long studentId,
            Sort sort);

    boolean
    existsByRoundCriterionIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
            Long roundCriterionId,
            Long mentorId);

    boolean
    existsByRoundCriterionIdAndRound_Phase_InternshipAssignments_Student_StudentId(
            Long roundCriterionId,
            Long studentId);


}