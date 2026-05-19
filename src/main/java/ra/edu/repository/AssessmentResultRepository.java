package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.AssessmentResult;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface AssessmentResultRepository extends JpaRepository<AssessmentResult, Long> {

    boolean existsByEvaluatedBy_UserId(Long userId);

    boolean existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(
            Long assignmentId,
            Long roundId,
            Long criterionId
    );


    Page<AssessmentResult> findAllByAssignment_AssignmentId(
            Long assignmentId,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByAssignment_Student_StudentId(
            Long studentId,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByAssignment_Mentor_MentorId(
            Long mentorId,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByAssignment_Phase_PhaseId(
            Long phaseId,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByAssignment_Status(
            InternshipAssignmentsStatus assignmentStatus,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByRound_RoundId(
            Long roundId,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByCriterion_CriterionId(
            Long criterionId,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByEvaluatedBy_UserId(
            Long evaluatedById,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByScore(
            BigDecimal score,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByScoreBetween(
            BigDecimal minScore,
            BigDecimal maxScore,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByScoreGreaterThanEqual(
            BigDecimal minScore,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByScoreLessThanEqual(
            BigDecimal maxScore,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByCommentsContainingIgnoreCase(
            String comments,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByEvaluationDate(
            LocalDateTime evaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByEvaluationDateBetween(
            LocalDateTime minEvaluationDate,
            LocalDateTime maxEvaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByEvaluationDateGreaterThanEqual(
            LocalDateTime minEvaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult> findAllByEvaluationDateLessThanEqual(
            LocalDateTime maxEvaluationDate,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Student_User_UsernameContainingIgnoreCase(
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_User_FullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_User_EmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_User_PhoneNumberContainingIgnoreCase(
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Mentor_User_UsernameContainingIgnoreCase(
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_User_FullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_User_EmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Phase_PhaseNameContainingIgnoreCase(
            String phaseName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByRound_RoundNameContainingIgnoreCase(
            String roundName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByCriterion_CriterionNameContainingIgnoreCase(
            String criterionName,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByEvaluatedBy_UsernameContainingIgnoreCase(
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByEvaluatedBy_FullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByEvaluatedBy_EmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByEvaluatedBy_PhoneNumberContainingIgnoreCase(
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_AssignmentId(
            Long mentorId,
            Long assignmentId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Student_StudentId(
            Long mentorId,
            Long studentId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Phase_PhaseId(
            Long mentorId,
            Long phaseId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Status(
            Long mentorId,
            InternshipAssignmentsStatus assignmentStatus,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndRound_RoundId(
            Long mentorId,
            Long roundId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndCriterion_CriterionId(
            Long mentorId,
            Long criterionId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_UserId(
            Long mentorId,
            Long evaluatedById,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndScore(
            Long mentorId,
            BigDecimal score,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndScoreBetween(
            Long mentorId,
            BigDecimal minScore,
            BigDecimal maxScore,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndScoreGreaterThanEqual(
            Long mentorId,
            BigDecimal minScore,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndScoreLessThanEqual(
            Long mentorId,
            BigDecimal maxScore,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndCommentsContainingIgnoreCase(
            Long mentorId,
            String comments,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluationDate(
            Long mentorId,
            LocalDateTime evaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluationDateBetween(
            Long mentorId,
            LocalDateTime minEvaluationDate,
            LocalDateTime maxEvaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluationDateGreaterThanEqual(
            Long mentorId,
            LocalDateTime minEvaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluationDateLessThanEqual(
            Long mentorId,
            LocalDateTime maxEvaluationDate,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_UsernameContainingIgnoreCase(
            Long mentorId,
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_FullNameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_EmailContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_PhoneNumberContainingIgnoreCase(
            Long mentorId,
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_UsernameContainingIgnoreCase(
            Long mentorId,
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_FullNameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_EmailContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
            Long mentorId,
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndAssignment_Phase_PhaseNameContainingIgnoreCase(
            Long mentorId,
            String phaseName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndRound_RoundNameContainingIgnoreCase(
            Long mentorId,
            String roundName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndCriterion_CriterionNameContainingIgnoreCase(
            Long mentorId,
            String criterionName,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_UsernameContainingIgnoreCase(
            Long mentorId,
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_FullNameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_EmailContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_PhoneNumberContainingIgnoreCase(
            Long mentorId,
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_AssignmentId(
            Long studentId,
            Long assignmentId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Mentor_MentorId(
            Long studentId,
            Long mentorId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Phase_PhaseId(
            Long studentId,
            Long phaseId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Status(
            Long studentId,
            InternshipAssignmentsStatus assignmentStatus,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndRound_RoundId(
            Long studentId,
            Long roundId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndCriterion_CriterionId(
            Long studentId,
            Long criterionId,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluatedBy_UserId(
            Long studentId,
            Long evaluatedById,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndScore(
            Long studentId,
            BigDecimal score,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndScoreBetween(
            Long studentId,
            BigDecimal minScore,
            BigDecimal maxScore,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndScoreGreaterThanEqual(
            Long studentId,
            BigDecimal minScore,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndScoreLessThanEqual(
            Long studentId,
            BigDecimal maxScore,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndCommentsContainingIgnoreCase(
            Long studentId,
            String comments,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluationDate(
            Long studentId,
            LocalDateTime evaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluationDateBetween(
            Long studentId,
            LocalDateTime minEvaluationDate,
            LocalDateTime maxEvaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluationDateGreaterThanEqual(
            Long studentId,
            LocalDateTime minEvaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluationDateLessThanEqual(
            Long studentId,
            LocalDateTime maxEvaluationDate,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Student_User_UsernameContainingIgnoreCase(
            Long studentId,
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Student_User_FullNameContainingIgnoreCase(
            Long studentId,
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Student_User_EmailContainingIgnoreCase(
            Long studentId,
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Student_User_PhoneNumberContainingIgnoreCase(
            Long studentId,
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_UsernameContainingIgnoreCase(
            Long studentId,
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_FullNameContainingIgnoreCase(
            Long studentId,
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_EmailContainingIgnoreCase(
            Long studentId,
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
            Long studentId,
            String phoneNumber,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndAssignment_Phase_PhaseNameContainingIgnoreCase(
            Long studentId,
            String phaseName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndRound_RoundNameContainingIgnoreCase(
            Long studentId,
            String roundName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndCriterion_CriterionNameContainingIgnoreCase(
            Long studentId,
            String criterionName,
            Pageable pageable
    );


    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluatedBy_UsernameContainingIgnoreCase(
            Long studentId,
            String username,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluatedBy_FullNameContainingIgnoreCase(
            Long studentId,
            String fullName,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluatedBy_EmailContainingIgnoreCase(
            Long studentId,
            String email,
            Pageable pageable
    );

    Page<AssessmentResult>
    findAllByAssignment_Student_StudentIdAndEvaluatedBy_PhoneNumberContainingIgnoreCase(
            Long studentId,
            String phoneNumber,
            Pageable pageable
    );


}