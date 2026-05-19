package ra.edu.service.impl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.edu.config.principal.UserPrincipal;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.AssessmentResultCreateRequest;
import ra.edu.dto.request.AssessmentResultUpdateRequest;
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.*;
import ra.edu.mapper.AssessmentResultMapper;
import ra.edu.repository.*;
import ra.edu.service.AssessmentResultService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AssessmentResultServiceImpl implements AssessmentResultService {

    private final AssessmentResultRepository assessmentResultRepository;

    private final StudentRepository studentRepository;

    private final MentorRepository mentorRepository;

    private final InternshipAssignmentRepository internshipAssignmentRepository;

    private final InternshipPhaseRepository internshipPhaseRepository;

    private final AssessmentRoundRepository assessmentRoundRepository;

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    private final UserRepository userRepository;

    private final AssessmentResultMapper assessmentResultMapper;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    @Override
    public PaginationResponse<AssessmentResultResponse> getAllResults(
            int page,
            int size,
            Long assignmentId,
            Long studentId,
            Long mentorId,
            Long phaseId,
            InternshipAssignmentsStatus assignmentStatus,
            Long roundId,
            Long criterionId,
            Long evaluatedById,
            String studentUsername,
            String studentFullName,
            String studentEmail,
            String studentPhoneNumber,
            String mentorUsername,
            String mentorFullName,
            String mentorEmail,
            String mentorPhoneNumber,
            String phaseName,
            String roundName,
            String criterionName,
            String evaluatedByUsername,
            String evaluatedByFullName,
            String evaluatedByEmail,
            String evaluatedByPhoneNumber,
            BigDecimal score,
            BigDecimal minScore,
            BigDecimal maxScore,
            String comments,
            LocalDateTime evaluationDate,
            LocalDateTime minEvaluationDate,
            LocalDateTime maxEvaluationDate) {

        if (minScore != null && maxScore != null
                && minScore.compareTo(maxScore) > 0) {

            throw new IllegalArgumentException(
                    "minScore không được lớn hơn maxScore");
        }

        if (minEvaluationDate != null && maxEvaluationDate != null
                && minEvaluationDate.isAfter(maxEvaluationDate)) {

            throw new IllegalArgumentException(
                    "minEvaluationDate không được sau maxEvaluationDate");
        }

        if (assignmentId != null
                && !internshipAssignmentRepository.existsById(assignmentId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy InternshipAssignment với ID = "
                            + assignmentId);
        }

        if (studentId != null && !studentRepository.existsById(studentId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy Student với ID = "
                            + studentId);
        }

        if (mentorId != null && !mentorRepository.existsById(mentorId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy Mentor với ID = "
                            + mentorId);
        }

        if (phaseId != null && !internshipPhaseRepository.existsById(phaseId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy InternshipPhase với ID = "
                            + phaseId);
        }

        if (roundId != null && !assessmentRoundRepository.existsById(roundId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy AssessmentRound với ID = "
                            + roundId);
        }

        if (criterionId != null
                && !evaluationCriteriaRepository.existsById(criterionId)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy EvaluationCriteria với ID = "
                            + criterionId);
        }

        if (evaluatedById != null && !userRepository.existsById(evaluatedById)) {

            throw new EntityNotFoundException(
                    "Không tìm thấy User với ID = "
                            + evaluatedById);
        }

        Pageable pageable = PageRequest.of(
                page - 1,
                size,
                Sort.by("resultId").descending());

        User currentUser = getCurrentUser();

        Page<AssessmentResult> resultPage;

        if (currentUser.getRole() == UserRole.ADMIN) {

            if (assignmentId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_AssignmentId(
                                        assignmentId, pageable);

            } else if (studentId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentId(
                                        studentId, pageable);

            } else if (mentorId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorId(
                                        mentorId, pageable);

            } else if (phaseId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Phase_PhaseId(
                                        phaseId, pageable);

            } else if (assignmentStatus != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Status(
                                        assignmentStatus, pageable);

            } else if (roundId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByRound_RoundId(
                                        roundId, pageable);

            } else if (criterionId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByCriterion_CriterionId(
                                        criterionId, pageable);

            } else if (evaluatedById != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluatedBy_UserId(
                                        evaluatedById, pageable);

            } else if (score != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByScore(
                                        score, pageable);

            } else if (minScore != null && maxScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByScoreBetween(
                                        minScore, maxScore, pageable);

            } else if (minScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByScoreGreaterThanEqual(
                                        minScore, pageable);

            } else if (maxScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByScoreLessThanEqual(
                                        maxScore, pageable);

            } else if (comments != null && !comments.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByCommentsContainingIgnoreCase(
                                        comments, pageable);

            } else if (evaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluationDate(
                                        evaluationDate, pageable);

            } else if (minEvaluationDate != null && maxEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluationDateBetween(minEvaluationDate,
                                        maxEvaluationDate, pageable);

            } else if (minEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluationDateGreaterThanEqual(
                                        minEvaluationDate, pageable);

            } else if (maxEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluationDateLessThanEqual(
                                        maxEvaluationDate, pageable);

            } else if (studentUsername != null && !studentUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_User_UsernameContainingIgnoreCase(
                                        studentUsername, pageable);

            } else if (studentFullName != null && !studentFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_User_FullNameContainingIgnoreCase(
                                        studentFullName, pageable);

            } else if (studentEmail != null && !studentEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_User_EmailContainingIgnoreCase(
                                        studentEmail, pageable);

            } else if (studentPhoneNumber != null
                    && !studentPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_User_PhoneNumberContainingIgnoreCase(
                                        studentPhoneNumber, pageable);

            } else if (mentorUsername != null && !mentorUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_User_UsernameContainingIgnoreCase(
                                        mentorUsername, pageable);

            } else if (mentorFullName != null && !mentorFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_User_FullNameContainingIgnoreCase(
                                        mentorFullName, pageable);

            } else if (mentorEmail != null && !mentorEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_User_EmailContainingIgnoreCase(
                                        mentorEmail, pageable);

            } else if (mentorPhoneNumber != null && !mentorPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
                                        mentorPhoneNumber, pageable);

            } else if (phaseName != null && !phaseName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Phase_PhaseNameContainingIgnoreCase(
                                        phaseName, pageable);

            } else if (roundName != null && !roundName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByRound_RoundNameContainingIgnoreCase(
                                        roundName, pageable);

            } else if (criterionName != null && !criterionName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByCriterion_CriterionNameContainingIgnoreCase(
                                        criterionName, pageable);

            } else if (evaluatedByUsername != null
                    && !evaluatedByUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluatedBy_UsernameContainingIgnoreCase(
                                        evaluatedByUsername, pageable);

            } else if (evaluatedByFullName != null
                    && !evaluatedByFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluatedBy_FullNameContainingIgnoreCase(
                                        evaluatedByFullName, pageable);

            } else if (evaluatedByEmail != null && !evaluatedByEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluatedBy_EmailContainingIgnoreCase(
                                        evaluatedByEmail, pageable);

            } else if (evaluatedByPhoneNumber != null
                    && !evaluatedByPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByEvaluatedBy_PhoneNumberContainingIgnoreCase(
                                        evaluatedByPhoneNumber, pageable);

            } else {

                resultPage =
                        assessmentResultRepository
                                .findAll(pageable);
            }
        } else if (currentUser.getRole() == UserRole.MENTOR) {

            Mentor mentor =
                    mentorRepository
                            .findByUser_UserId(currentUser.getUserId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role MENTOR"));

            if (assignmentId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_AssignmentId(
                                        mentor.getMentorId(),
                                        assignmentId, pageable);

            } else if (studentId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_StudentId(
                                        mentor.getMentorId(),
                                        studentId, pageable);

            } else if (phaseId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Phase_PhaseId(
                                        mentor.getMentorId(),
                                        phaseId, pageable);

            } else if (assignmentStatus != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Status(
                                        mentor.getMentorId(),
                                        assignmentStatus, pageable);

            } else if (roundId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndRound_RoundId(
                                        mentor.getMentorId(),
                                        roundId, pageable);

            } else if (criterionId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndCriterion_CriterionId(
                                        mentor.getMentorId(),
                                        criterionId, pageable);

            } else if (evaluatedById != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_UserId(
                                        mentor.getMentorId(),
                                        evaluatedById, pageable);

            } else if (score != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndScore(
                                        mentor.getMentorId(),
                                        score, pageable);

            } else if (minScore != null && maxScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndScoreBetween(
                                        mentor.getMentorId(),
                                        minScore, maxScore, pageable);

            } else if (minScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndScoreGreaterThanEqual(
                                        mentor.getMentorId(),
                                        minScore, pageable);

            } else if (maxScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndScoreLessThanEqual(
                                        mentor.getMentorId(),
                                        maxScore, pageable);

            } else if (comments != null && !comments.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndCommentsContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        comments, pageable);

            } else if (evaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluationDate(
                                        mentor.getMentorId(),
                                        evaluationDate, pageable);

            } else if (minEvaluationDate != null && maxEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluationDateBetween(
                                        mentor.getMentorId(), minEvaluationDate,
                                        maxEvaluationDate, pageable);

            } else if (minEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluationDateGreaterThanEqual(
                                        mentor.getMentorId(),
                                        minEvaluationDate, pageable);

            } else if (maxEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluationDateLessThanEqual(
                                        mentor.getMentorId(),
                                        maxEvaluationDate, pageable);

            } else if (studentUsername != null && !studentUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_UsernameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentUsername, pageable);

            } else if (studentFullName != null && !studentFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_FullNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentFullName, pageable);

            } else if (studentEmail != null && !studentEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_EmailContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentEmail, pageable);

            } else if (studentPhoneNumber != null
                    && !studentPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_PhoneNumberContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        studentPhoneNumber, pageable);

            } else if (mentorUsername != null && !mentorUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_UsernameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorUsername, pageable);

            } else if (mentorFullName != null && !mentorFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_FullNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorFullName, pageable);

            } else if (mentorEmail != null && !mentorEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_EmailContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorEmail, pageable);

            } else if (mentorPhoneNumber != null && !mentorPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        mentorPhoneNumber, pageable);

            } else if (phaseName != null && !phaseName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndAssignment_Phase_PhaseNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        phaseName, pageable);

            } else if (roundName != null && !roundName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndRound_RoundNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        roundName, pageable);

            } else if (criterionName != null && !criterionName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndCriterion_CriterionNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        criterionName, pageable);

            } else if (evaluatedByUsername != null
                    && !evaluatedByUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_UsernameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        evaluatedByUsername, pageable);

            } else if (evaluatedByFullName != null
                    && !evaluatedByFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_FullNameContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        evaluatedByFullName, pageable);

            } else if (evaluatedByEmail != null
                    && !evaluatedByEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_EmailContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        evaluatedByEmail, pageable);

            } else if (evaluatedByPhoneNumber != null
                    && !evaluatedByPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_PhoneNumberContainingIgnoreCase(
                                        mentor.getMentorId(),
                                        evaluatedByPhoneNumber, pageable);

            } else {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Mentor_MentorId(
                                        mentor.getMentorId(), pageable);
            }
        } else {

            Student student =
                    studentRepository
                            .findByUser_UserId(currentUser.getUserId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role STUDENT"));

            if (assignmentId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_AssignmentId(
                                        student.getStudentId(), assignmentId, pageable);

            } else if (mentorId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_MentorId(
                                        student.getStudentId(),
                                        mentorId, pageable);

            } else if (phaseId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Phase_PhaseId(
                                        student.getStudentId(),
                                        phaseId, pageable);

            } else if (assignmentStatus != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Status(
                                        student.getStudentId(),
                                        assignmentStatus, pageable);

            } else if (roundId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndRound_RoundId(
                                        student.getStudentId(),
                                        roundId, pageable);

            } else if (criterionId != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndCriterion_CriterionId(
                                        student.getStudentId(),
                                        criterionId, pageable);

            } else if (evaluatedById != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluatedBy_UserId(
                                        student.getStudentId(),
                                        evaluatedById, pageable);

            } else if (score != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndScore(
                                        student.getStudentId(),
                                        score, pageable);

            } else if (minScore != null && maxScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndScoreBetween(
                                        student.getStudentId(),
                                        minScore, maxScore, pageable);

            } else if (minScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndScoreGreaterThanEqual(
                                        student.getStudentId(),
                                        minScore, pageable);

            } else if (maxScore != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndScoreLessThanEqual(
                                        student.getStudentId(),
                                        maxScore, pageable);

            } else if (comments != null && !comments.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndCommentsContainingIgnoreCase(
                                        student.getStudentId(),
                                        comments, pageable);

            } else if (evaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluationDate(
                                        student.getStudentId(),
                                        evaluationDate, pageable);

            } else if (minEvaluationDate != null && maxEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluationDateBetween(
                                        student.getStudentId(), minEvaluationDate,
                                        maxEvaluationDate, pageable);

            } else if (minEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluationDateGreaterThanEqual(
                                        student.getStudentId(),
                                        minEvaluationDate, pageable);

            } else if (maxEvaluationDate != null) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluationDateLessThanEqual(
                                        student.getStudentId(),
                                        maxEvaluationDate, pageable);

            } else if (studentUsername != null && !studentUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_UsernameContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentUsername, pageable);

            } else if (studentFullName != null && !studentFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_FullNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentFullName, pageable);

            } else if (studentEmail != null && !studentEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_EmailContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentEmail, pageable);

            } else if (studentPhoneNumber != null
                    && !studentPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_PhoneNumberContainingIgnoreCase(
                                        student.getStudentId(),
                                        studentPhoneNumber, pageable);

            } else if (mentorUsername != null && !mentorUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_UsernameContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorUsername, pageable);

            } else if (mentorFullName != null && !mentorFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_FullNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorFullName, pageable);

            } else if (mentorEmail != null && !mentorEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_EmailContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorEmail, pageable);

            } else if (mentorPhoneNumber != null && !mentorPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
                                        student.getStudentId(),
                                        mentorPhoneNumber, pageable);

            } else if (phaseName != null && !phaseName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndAssignment_Phase_PhaseNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        phaseName, pageable);

            } else if (roundName != null && !roundName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndRound_RoundNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        roundName, pageable);

            } else if (criterionName != null && !criterionName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndCriterion_CriterionNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        criterionName, pageable);

            } else if (evaluatedByUsername != null
                    && !evaluatedByUsername.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluatedBy_UsernameContainingIgnoreCase(
                                        student.getStudentId(),
                                        evaluatedByUsername, pageable);

            } else if (evaluatedByFullName != null
                    && !evaluatedByFullName.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluatedBy_FullNameContainingIgnoreCase(
                                        student.getStudentId(),
                                        evaluatedByFullName, pageable);

            } else if (evaluatedByEmail != null
                    && !evaluatedByEmail.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluatedBy_EmailContainingIgnoreCase(
                                        student.getStudentId(),
                                        evaluatedByEmail, pageable);

            } else if (evaluatedByPhoneNumber != null
                    && !evaluatedByPhoneNumber.isBlank()) {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentIdAndEvaluatedBy_PhoneNumberContainingIgnoreCase(
                                        student.getStudentId(),
                                        evaluatedByPhoneNumber, pageable);

            } else {

                resultPage =
                        assessmentResultRepository
                                .findAllByAssignment_Student_StudentId(
                                        student.getStudentId(),
                                        pageable);
            }
        }

        List<AssessmentResultResponse> items =
                resultPage.getContent()
                        .stream()
                        .map(assessmentResultMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(resultPage.getTotalPages())
                .totalItems(resultPage.getTotalElements())
                .build();

        return PaginationResponse.<AssessmentResultResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public AssessmentResultResponse getResultById(Long id) {

        AssessmentResult result =
                assessmentResultRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy AssessmentResult với ID = "
                                                + id));

        User currentUser = getCurrentUser();

        if (currentUser.getRole() == UserRole.ADMIN) {

            return assessmentResultMapper.toResponse(result);
        }

        if (currentUser.getRole() == UserRole.MENTOR) {

            Mentor mentor =
                    mentorRepository.findByUser_UserId(
                                    currentUser.getUserId())
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "User ID = "
                                                    + currentUser.getUserId()
                                                    + " chưa được liên kết với role MENTOR"));

            if (!result.getAssignment()
                    .getMentor()
                    .getMentorId()
                    .equals(mentor.getMentorId())) {

                throw new AccessDeniedException(
                        "Mentor ID = "
                                + mentor.getMentorId()
                                + " không có quyền xem AssessmentResult ID = "
                                + result.getResultId());
            }

            return assessmentResultMapper.toResponse(result);
        }

        Student student =
                studentRepository.findByUser_UserId(
                                currentUser.getUserId()
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role STUDENT"));

        if (!result.getAssignment()
                .getStudent()
                .getStudentId()
                .equals(student.getStudentId())) {

            throw new AccessDeniedException(
                    "Student ID = "
                            + student.getStudentId()
                            + " không có quyền xem AssessmentResult ID = "
                            + result.getResultId());
        }

        return assessmentResultMapper.toResponse(result);
    }

    @Override
    public AssessmentResultResponse createResult(
            AssessmentResultCreateRequest request) {

        User currentUser = getCurrentUser();


        InternshipAssignment assignment =
                internshipAssignmentRepository.findById(request.getAssignmentId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = "
                                                + request.getAssignmentId()));


        Mentor mentor =
                mentorRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role MENTOR"));

        if (!assignment.getMentor().getMentorId()
                .equals(mentor.getMentorId())) {

            throw new AccessDeniedException(
                    "Mentor ID = "
                            + mentor.getMentorId()
                            + " không được phân công cho Assignment ID = "
                            + assignment.getAssignmentId());
        }

        AssessmentRound round =
                assessmentRoundRepository.findById(request.getRoundId())
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy AssessmentRound với ID = "
                                                + request.getRoundId()));

        EvaluationCriteria criterion =
                evaluationCriteriaRepository.findById(
                                request.getCriterionId()
                        )
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy EvaluationCriteria với ID = "
                                                + request.getCriterionId()));


        if (assessmentResultRepository
                .existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(
                        assignment.getAssignmentId(),
                        round.getRoundId(),
                        criterion.getCriterionId())) {

            throw new IllegalStateException(
                    "AssessmentResult already exists with: "
                            + " assignmentId =" + assignment.getAssignmentId()
                            + ", roundId =" + round.getRoundId()
                            + ", criterionId =" + criterion.getCriterionId());
        }

        AssessmentResult result = assessmentResultMapper.toEntity(request);

        result.setAssignment(assignment);

        result.setRound(round);

        result.setCriterion(criterion);

        result.setEvaluatedBy(currentUser);

        result.setEvaluationDate(LocalDateTime.now());

        result.setCreatedAt(LocalDateTime.now());

        result.setUpdatedAt(LocalDateTime.now());

        assessmentResultRepository.save(result);

        return assessmentResultMapper.toResponse(result);
    }

    @Override
    public AssessmentResultResponse updateResult(
            Long id,
            AssessmentResultUpdateRequest request) {

        User currentUser = getCurrentUser();

        AssessmentResult result =
                assessmentResultRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy AssessmentResult với ID = "
                                                + id));


        if (!result.getEvaluatedBy().getUserId()
                .equals(currentUser.getUserId())) {

            throw new AccessDeniedException(
                    "User ID = "
                            + currentUser.getUserId()
                            + " không có quyền cập nhật AssessmentResult ID = "
                            + result.getResultId());
        }

        assessmentResultMapper.updateEntityFromDto(request, result);

        result.setUpdatedAt(LocalDateTime.now());

        assessmentResultRepository.save(result);

        return assessmentResultMapper.toResponse(result);
    }

    @Override
    public AssessmentResultResponse deleteResult(Long id) {

        User currentUser = getCurrentUser();


        AssessmentResult result =
                assessmentResultRepository.findById(id)
                        .orElseThrow(() ->
                                new EntityNotFoundException(
                                        "Không tìm thấy AssessmentResult với ID = "
                                                + id));


        if (!result.getEvaluatedBy().getUserId()
                .equals(currentUser.getUserId())) {

            throw new AccessDeniedException(
                    "User ID = "
                            + currentUser.getUserId()
                            + " không có quyền xóa AssessmentResult ID = "
                            + result.getResultId());
        }

        AssessmentResultResponse response =
                assessmentResultMapper.toResponse(result);

        assessmentResultRepository.delete(result);

        return response;
    }


}



