package ra.edu.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ra.edu.config.principal.UserPrincipal;
import ra.edu.dto.Pagination;
import ra.edu.dto.request.AssessmentResultCreateRequest;
import ra.edu.dto.request.AssessmentResultFilterRequest;
import ra.edu.dto.request.AssessmentResultUpdateRequest;
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.entity.*;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
import ra.edu.mapper.AssessmentResultMapper;
import ra.edu.repository.*;
import ra.edu.service.AssessmentResultService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final RoundCriteriaRepository roundCriteriaRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    private void validateFilterIds(AssessmentResultFilterRequest filter) {

        if (filter.getAssignmentId() != null
                && !internshipAssignmentRepository.existsById(filter.getAssignmentId())) {
            throw new NotFoundException(
                    "Không tìm thấy InternshipAssignment với ID = " + filter.getAssignmentId());
        }

        if (filter.getPhaseId() != null
                && !internshipPhaseRepository.existsById(filter.getPhaseId())) {
            throw new NotFoundException(
                    "Không tìm thấy InternshipPhase với ID = " + filter.getPhaseId());
        }

        if (filter.getRoundId() != null
                && !assessmentRoundRepository.existsById(filter.getRoundId())) {
            throw new NotFoundException(
                    "Không tìm thấy AssessmentRound với ID = " + filter.getRoundId());
        }

        if (filter.getCriterionId() != null
                && !evaluationCriteriaRepository.existsById(filter.getCriterionId())) {
            throw new NotFoundException(
                    "Không tìm thấy EvaluationCriteria với ID = " + filter.getCriterionId());
        }

    }

    private void validateScoreAndDate(AssessmentResultFilterRequest filter) {

        if (filter.getMinScore() != null && filter.getMaxScore() != null
                && filter.getMinScore().compareTo(filter.getMaxScore()) > 0) {

            throw new BadRequestException(
                    "minScore không được lớn hơn maxScore");
        }

        if (filter.getMinEvaluationDate() != null && filter.getMaxEvaluationDate() != null
                && filter.getMinEvaluationDate().isAfter(filter.getMaxEvaluationDate())) {

            throw new BadRequestException(
                    "minEvaluationDate không được sau maxEvaluationDate");
        }
    }

    private Page<AssessmentResult> getAllForAdmin(
            AssessmentResultFilterRequest filter,
            Pageable pageable) {

        if (filter.getAssignmentId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_AssignmentId(
                            filter.getAssignmentId(), pageable);

        } else if (filter.getStudentId() != null) {

            userRepository.findById(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Student với ID = " + filter.getStudentId()));

            studentRepository.findByUser_UserId(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getStudentId() + " chưa được liên kết với role STUDENT"));

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentId(
                            filter.getStudentId(), pageable);

        } else if (filter.getMentorId() != null) {

            userRepository.findById(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Mentor với ID = " + filter.getMentorId()));

            mentorRepository.findByUser_UserId(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getMentorId() + " chưa được liên kết với role MENTOR"));

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorId(
                            filter.getMentorId(), pageable);

        } else if (filter.getPhaseId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Phase_PhaseId(
                            filter.getPhaseId(), pageable);

        } else if (filter.getAssignmentStatus() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Status(
                            filter.getAssignmentStatus(), pageable);

        } else if (filter.getRoundId() != null) {

            return assessmentResultRepository
                    .findAllByRound_RoundId(
                            filter.getRoundId(), pageable);

        } else if (filter.getCriterionId() != null) {

            return assessmentResultRepository
                    .findAllByCriterion_CriterionId(
                            filter.getCriterionId(), pageable);

        } else if (filter.getEvaluatedById() != null) {

            userRepository.findById(filter.getEvaluatedById())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy User với ID = " + filter.getEvaluatedById()));

            mentorRepository.findByUser_UserId(filter.getEvaluatedById())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getEvaluatedById() + " chưa được liên kết với role MENTOR"));

            return assessmentResultRepository
                    .findAllByEvaluatedBy_UserId(
                            filter.getEvaluatedById(), pageable);

        } else if (filter.getScore() != null) {

            return assessmentResultRepository
                    .findAllByScore(
                            filter.getScore(), pageable);

        } else if (filter.getMinScore() != null && filter.getMaxScore() != null) {

            return assessmentResultRepository
                    .findAllByScoreBetween(
                            filter.getMinScore(),
                            filter.getMaxScore(),
                            pageable);

        } else if (filter.getMinScore() != null) {

            return assessmentResultRepository
                    .findAllByScoreGreaterThanEqual(
                            filter.getMinScore(), pageable);

        } else if (filter.getMaxScore() != null) {

            return assessmentResultRepository
                    .findAllByScoreLessThanEqual(
                            filter.getMaxScore(), pageable);

        } else if (filter.getComments() != null && !filter.getComments().isBlank()) {

            return assessmentResultRepository
                    .findAllByCommentsContainingIgnoreCase(
                            filter.getComments(), pageable);

        } else if (filter.getEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByEvaluationDate(
                            filter.getEvaluationDate(), pageable);

        } else if (filter.getMinEvaluationDate() != null
                && filter.getMaxEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByEvaluationDateBetween(
                            filter.getMinEvaluationDate(),
                            filter.getMaxEvaluationDate(),
                            pageable);

        } else if (filter.getMinEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByEvaluationDateGreaterThanEqual(
                            filter.getMinEvaluationDate(), pageable);

        } else if (filter.getMaxEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByEvaluationDateLessThanEqual(
                            filter.getMaxEvaluationDate(), pageable);

        } else if (filter.getStudentUsername() != null && !filter.getStudentUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_User_UsernameContainingIgnoreCase(
                            filter.getStudentUsername(), pageable);

        } else if (filter.getStudentFullName() != null && !filter.getStudentFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_User_FullNameContainingIgnoreCase(
                            filter.getStudentFullName(), pageable);

        } else if (filter.getStudentEmail() != null && !filter.getStudentEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_User_EmailContainingIgnoreCase(
                            filter.getStudentEmail(), pageable);

        } else if (filter.getStudentPhoneNumber() != null && !filter.getStudentPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_User_PhoneNumberContainingIgnoreCase(
                            filter.getStudentPhoneNumber(), pageable);

        } else if (filter.getMentorUsername() != null && !filter.getMentorUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_User_UsernameContainingIgnoreCase(
                            filter.getMentorUsername(), pageable);

        } else if (filter.getMentorFullName() != null && !filter.getMentorFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_User_FullNameContainingIgnoreCase(
                            filter.getMentorFullName(), pageable);

        } else if (filter.getMentorEmail() != null && !filter.getMentorEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_User_EmailContainingIgnoreCase(
                            filter.getMentorEmail(), pageable);

        } else if (filter.getMentorPhoneNumber() != null && !filter.getMentorPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
                            filter.getMentorPhoneNumber(), pageable);

        } else if (filter.getPhaseName() != null && !filter.getPhaseName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Phase_PhaseNameContainingIgnoreCase(
                            filter.getPhaseName(), pageable);

        } else if (filter.getRoundName() != null && !filter.getRoundName().isBlank()) {

            return assessmentResultRepository
                    .findAllByRound_RoundNameContainingIgnoreCase(
                            filter.getRoundName(), pageable);

        } else if (filter.getCriterionName() != null && !filter.getCriterionName().isBlank()) {

            return assessmentResultRepository
                    .findAllByCriterion_CriterionNameContainingIgnoreCase(
                            filter.getCriterionName(), pageable);

        } else if (filter.getEvaluatedByUsername() != null && !filter.getEvaluatedByUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByEvaluatedBy_UsernameContainingIgnoreCase(
                            filter.getEvaluatedByUsername(), pageable);

        } else if (filter.getEvaluatedByFullName() != null && !filter.getEvaluatedByFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByEvaluatedBy_FullNameContainingIgnoreCase(
                            filter.getEvaluatedByFullName(), pageable);

        } else if (filter.getEvaluatedByEmail() != null && !filter.getEvaluatedByEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByEvaluatedBy_EmailContainingIgnoreCase(
                            filter.getEvaluatedByEmail(), pageable);

        } else if (filter.getEvaluatedByPhoneNumber() != null && !filter.getEvaluatedByPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByEvaluatedBy_PhoneNumberContainingIgnoreCase(
                            filter.getEvaluatedByPhoneNumber(), pageable);
        }

        return assessmentResultRepository.findAll(pageable);
    }

    private Page<AssessmentResult> getAllForMentor(
            AssessmentResultFilterRequest filter,
            Pageable pageable,
            User currentUser) {

        Mentor mentor = mentorRepository
                .findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = " + currentUser.getUserId()
                                        + " chưa được liên kết với role MENTOR"));

        Long mentorId = mentor.getMentorId();

        if (filter.getAssignmentId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_AssignmentId(
                            mentorId,
                            filter.getAssignmentId(),
                            pageable);

        } else if (filter.getStudentId() != null) {

            userRepository.findById(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Student với ID = " + filter.getStudentId()));

            studentRepository.findByUser_UserId(filter.getStudentId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getStudentId() + " chưa được liên kết với role STUDENT"));

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_StudentId(
                            mentorId,
                            filter.getStudentId(),
                            pageable);

        } else if (filter.getPhaseId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Phase_PhaseId(
                            mentorId,
                            filter.getPhaseId(),
                            pageable);

        } else if (filter.getAssignmentStatus() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Status(
                            mentorId,
                            filter.getAssignmentStatus(),
                            pageable);

        } else if (filter.getRoundId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndRound_RoundId(
                            mentorId,
                            filter.getRoundId(),
                            pageable);

        } else if (filter.getCriterionId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndCriterion_CriterionId(
                            mentorId,
                            filter.getCriterionId(),
                            pageable);

        } else if (filter.getEvaluatedById() != null) {

            userRepository.findById(filter.getEvaluatedById())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy User với ID = " + filter.getEvaluatedById()));

            mentorRepository.findByUser_UserId(filter.getEvaluatedById())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getEvaluatedById() + " chưa được liên kết với role MENTOR"));

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_UserId(
                            mentorId,
                            filter.getEvaluatedById(),
                            pageable);

        } else if (filter.getScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndScore(
                            mentorId,
                            filter.getScore(),
                            pageable);

        } else if (filter.getMinScore() != null && filter.getMaxScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndScoreBetween(
                            mentorId,
                            filter.getMinScore(),
                            filter.getMaxScore(),
                            pageable);

        } else if (filter.getMinScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndScoreGreaterThanEqual(
                            mentorId,
                            filter.getMinScore(),
                            pageable);

        } else if (filter.getMaxScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndScoreLessThanEqual(
                            mentorId,
                            filter.getMaxScore(),
                            pageable);

        } else if (filter.getComments() != null && !filter.getComments().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndCommentsContainingIgnoreCase(
                            mentorId,
                            filter.getComments(),
                            pageable);

        } else if (filter.getEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluationDate(
                            mentorId,
                            filter.getEvaluationDate(),
                            pageable);

        } else if (filter.getMinEvaluationDate() != null && filter.getMaxEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluationDateBetween(
                            mentorId,
                            filter.getMinEvaluationDate(),
                            filter.getMaxEvaluationDate(),
                            pageable);

        } else if (filter.getMinEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluationDateGreaterThanEqual(
                            mentorId,
                            filter.getMinEvaluationDate(),
                            pageable);

        } else if (filter.getMaxEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluationDateLessThanEqual(
                            mentorId,
                            filter.getMaxEvaluationDate(),
                            pageable);

        } else if (filter.getStudentUsername() != null && !filter.getStudentUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_UsernameContainingIgnoreCase(
                            mentorId,
                            filter.getStudentUsername(),
                            pageable);

        } else if (filter.getStudentFullName() != null && !filter.getStudentFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_FullNameContainingIgnoreCase(
                            mentorId,
                            filter.getStudentFullName(),
                            pageable);

        } else if (filter.getStudentEmail() != null && !filter.getStudentEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_EmailContainingIgnoreCase(
                            mentorId,
                            filter.getStudentEmail(),
                            pageable);

        } else if (filter.getStudentPhoneNumber() != null && !filter.getStudentPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Student_User_PhoneNumberContainingIgnoreCase(
                            mentorId,
                            filter.getStudentPhoneNumber(),
                            pageable);

        } else if (filter.getMentorUsername() != null && !filter.getMentorUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_UsernameContainingIgnoreCase(
                            mentorId,
                            filter.getMentorUsername(),
                            pageable);

        } else if (filter.getMentorFullName() != null && !filter.getMentorFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_FullNameContainingIgnoreCase(
                            mentorId,
                            filter.getMentorFullName(),
                            pageable);

        } else if (filter.getMentorEmail() != null && !filter.getMentorEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_EmailContainingIgnoreCase(
                            mentorId,
                            filter.getMentorEmail(),
                            pageable);

        } else if (filter.getMentorPhoneNumber() != null && !filter.getMentorPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
                            mentorId,
                            filter.getMentorPhoneNumber(),
                            pageable);

        } else if (filter.getPhaseName() != null && !filter.getPhaseName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndAssignment_Phase_PhaseNameContainingIgnoreCase(
                            mentorId,
                            filter.getPhaseName(),
                            pageable);

        } else if (filter.getRoundName() != null && !filter.getRoundName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndRound_RoundNameContainingIgnoreCase(
                            mentorId,
                            filter.getRoundName(),
                            pageable);

        } else if (filter.getCriterionName() != null && !filter.getCriterionName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndCriterion_CriterionNameContainingIgnoreCase(
                            mentorId,
                            filter.getCriterionName(),
                            pageable);

        } else if (filter.getEvaluatedByUsername() != null && !filter.getEvaluatedByUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_UsernameContainingIgnoreCase(
                            mentorId,
                            filter.getEvaluatedByUsername(),
                            pageable);

        } else if (filter.getEvaluatedByFullName() != null && !filter.getEvaluatedByFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_FullNameContainingIgnoreCase(
                            mentorId,
                            filter.getEvaluatedByFullName(),
                            pageable);

        } else if (filter.getEvaluatedByEmail() != null && !filter.getEvaluatedByEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_EmailContainingIgnoreCase(
                            mentorId,
                            filter.getEvaluatedByEmail(),
                            pageable);

        } else if (filter.getEvaluatedByPhoneNumber() != null && !filter.getEvaluatedByPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Mentor_MentorIdAndEvaluatedBy_PhoneNumberContainingIgnoreCase(
                            mentorId,
                            filter.getEvaluatedByPhoneNumber(),
                            pageable);
        }

        return assessmentResultRepository
                .findAllByAssignment_Mentor_MentorId(
                        mentorId,
                        pageable);
    }

    private Page<AssessmentResult> getAllForStudent(
            AssessmentResultFilterRequest filter,
            Pageable pageable,
            User currentUser) {

        Student student = studentRepository
                .findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = " + currentUser.getUserId()
                                        + " chưa được liên kết với role STUDENT"));

        Long studentId = student.getStudentId();

        if (filter.getAssignmentId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_AssignmentId(
                            studentId,
                            filter.getAssignmentId(),
                            pageable);

        } else if (filter.getMentorId() != null) {

            userRepository.findById(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy Mentor với ID = " + filter.getMentorId()));

            mentorRepository.findByUser_UserId(filter.getMentorId())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getMentorId() + " chưa được liên kết với role MENTOR"));

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_MentorId(
                            studentId,
                            filter.getMentorId(),
                            pageable);

        } else if (filter.getPhaseId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Phase_PhaseId(
                            studentId,
                            filter.getPhaseId(),
                            pageable);

        } else if (filter.getAssignmentStatus() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Status(
                            studentId,
                            filter.getAssignmentStatus(),
                            pageable);

        } else if (filter.getRoundId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndRound_RoundId(
                            studentId,
                            filter.getRoundId(),
                            pageable);

        } else if (filter.getCriterionId() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndCriterion_CriterionId(
                            studentId,
                            filter.getCriterionId(),
                            pageable);

        } else if (filter.getEvaluatedById() != null) {

            userRepository.findById(filter.getEvaluatedById())
                    .orElseThrow(() -> new NotFoundException("Không tìm thấy User với ID = " + filter.getEvaluatedById()));

            mentorRepository.findByUser_UserId(filter.getEvaluatedById())
                    .orElseThrow(() -> new NotFoundException("User ID = " + filter.getEvaluatedById() + " chưa được liên kết với role MENTOR"));

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluatedBy_UserId(
                            studentId,
                            filter.getEvaluatedById(),
                            pageable);

        } else if (filter.getScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndScore(
                            studentId,
                            filter.getScore(),
                            pageable);

        } else if (filter.getMinScore() != null && filter.getMaxScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndScoreBetween(
                            studentId,
                            filter.getMinScore(),
                            filter.getMaxScore(),
                            pageable);

        } else if (filter.getMinScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndScoreGreaterThanEqual(
                            studentId,
                            filter.getMinScore(),
                            pageable);

        } else if (filter.getMaxScore() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndScoreLessThanEqual(
                            studentId,
                            filter.getMaxScore(),
                            pageable);

        } else if (filter.getComments() != null && !filter.getComments().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndCommentsContainingIgnoreCase(
                            studentId,
                            filter.getComments(),
                            pageable);

        } else if (filter.getEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluationDate(
                            studentId,
                            filter.getEvaluationDate(),
                            pageable);

        } else if (filter.getMinEvaluationDate() != null
                && filter.getMaxEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluationDateBetween(
                            studentId,
                            filter.getMinEvaluationDate(),
                            filter.getMaxEvaluationDate(),
                            pageable);

        } else if (filter.getMinEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluationDateGreaterThanEqual(
                            studentId,
                            filter.getMinEvaluationDate(),
                            pageable);

        } else if (filter.getMaxEvaluationDate() != null) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluationDateLessThanEqual(
                            studentId,
                            filter.getMaxEvaluationDate(),
                            pageable);

        } else if (filter.getStudentUsername() != null && !filter.getStudentUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_UsernameContainingIgnoreCase(
                            studentId,
                            filter.getStudentUsername(),
                            pageable);

        } else if (filter.getStudentFullName() != null && !filter.getStudentFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_FullNameContainingIgnoreCase(
                            studentId,
                            filter.getStudentFullName(),
                            pageable);

        } else if (filter.getStudentEmail() != null && !filter.getStudentEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_EmailContainingIgnoreCase(
                            studentId,
                            filter.getStudentEmail(),
                            pageable);

        } else if (filter.getStudentPhoneNumber() != null && !filter.getStudentPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Student_User_PhoneNumberContainingIgnoreCase(
                            studentId,
                            filter.getStudentPhoneNumber(),
                            pageable);

        } else if (filter.getMentorUsername() != null && !filter.getMentorUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_UsernameContainingIgnoreCase(
                            studentId,
                            filter.getMentorUsername(),
                            pageable);

        } else if (filter.getMentorFullName() != null && !filter.getMentorFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_FullNameContainingIgnoreCase(
                            studentId,
                            filter.getMentorFullName(),
                            pageable);

        } else if (filter.getMentorEmail() != null && !filter.getMentorEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_EmailContainingIgnoreCase(
                            studentId,
                            filter.getMentorEmail(),
                            pageable);

        } else if (filter.getMentorPhoneNumber() != null && !filter.getMentorPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Mentor_User_PhoneNumberContainingIgnoreCase(
                            studentId,
                            filter.getMentorPhoneNumber(),
                            pageable);

        } else if (filter.getPhaseName() != null && !filter.getPhaseName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndAssignment_Phase_PhaseNameContainingIgnoreCase(
                            studentId,
                            filter.getPhaseName(),
                            pageable);

        } else if (filter.getRoundName() != null && !filter.getRoundName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndRound_RoundNameContainingIgnoreCase(
                            studentId,
                            filter.getRoundName(),
                            pageable);

        } else if (filter.getCriterionName() != null && !filter.getCriterionName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndCriterion_CriterionNameContainingIgnoreCase(
                            studentId,
                            filter.getCriterionName(),
                            pageable);

        } else if (filter.getEvaluatedByUsername() != null && !filter.getEvaluatedByUsername().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluatedBy_UsernameContainingIgnoreCase(
                            studentId,
                            filter.getEvaluatedByUsername(),
                            pageable);

        } else if (filter.getEvaluatedByFullName() != null && !filter.getEvaluatedByFullName().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluatedBy_FullNameContainingIgnoreCase(
                            studentId,
                            filter.getEvaluatedByFullName(),
                            pageable);

        } else if (filter.getEvaluatedByEmail() != null && !filter.getEvaluatedByEmail().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluatedBy_EmailContainingIgnoreCase(
                            studentId,
                            filter.getEvaluatedByEmail(),
                            pageable);

        } else if (filter.getEvaluatedByPhoneNumber() != null && !filter.getEvaluatedByPhoneNumber().isBlank()) {

            return assessmentResultRepository
                    .findAllByAssignment_Student_StudentIdAndEvaluatedBy_PhoneNumberContainingIgnoreCase(
                            studentId,
                            filter.getEvaluatedByPhoneNumber(),
                            pageable);
        }

        return assessmentResultRepository.findAllByAssignment_Student_StudentId(
                studentId,
                pageable);
    }

    private AssessmentResultResponse toResponse(AssessmentResult result) {
        return assessmentResultMapper.toResponse(result);
    }

    @Override
    public PaginationResponse<AssessmentResultResponse> getAllResult(
            AssessmentResultFilterRequest filter) {

        validateFilterIds(filter);

        validateScoreAndDate(filter);

        Pageable pageable = PageRequest.of(
                filter.getPage() - 1,
                filter.getSize(),
                Sort.by("resultId").descending());

        User currentUser = getCurrentUser();

        Page<AssessmentResult> resultPage;

        switch (currentUser.getRole()) {

            case ADMIN -> resultPage = getAllForAdmin(filter, pageable);

            case MENTOR -> resultPage = getAllForMentor(
                    filter,
                    pageable,
                    currentUser);

            case STUDENT -> resultPage = getAllForStudent(
                    filter,
                    pageable,
                    currentUser);

            default -> throw new ForbiddenException(
                    "không được phép truy cập");
        }

        List<AssessmentResultResponse> items =
                resultPage.getContent()
                        .stream()
                        .map(assessmentResultMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(filter.getPage())
                .pageSize(filter.getSize())
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

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {

                AssessmentResult result =
                        assessmentResultRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy AssessmentResult với ID = "
                                                        + id));

                return toResponse(result);
            }

            case MENTOR -> {

                Mentor mentor =
                        mentorRepository.findByUser_UserId(currentUser.getUserId())
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "User ID = "
                                                        + currentUser.getUserId()
                                                        + " chưa được liên kết với role MENTOR"));

                AssessmentResult result =
                        assessmentResultRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy AssessmentResult với ID = "
                                                        + id));

                Long ownerMentorId =
                        result.getAssignment()
                                .getMentor()
                                .getMentorId();

                if (!ownerMentorId.equals(mentor.getMentorId())) {

                    throw new NotFoundException("Không tìm thấy AssessmentResult với ID = " + id);
                }

                return toResponse(result);
            }

            case STUDENT -> {

                Student student =
                        studentRepository.findByUser_UserId(currentUser.getUserId())
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "User ID = "
                                                        + currentUser.getUserId()
                                                        + " chưa được liên kết với role STUDENT"));

                AssessmentResult result =
                        assessmentResultRepository.findById(id)
                                .orElseThrow(() ->
                                        new NotFoundException(
                                                "Không tìm thấy AssessmentResult với ID = "
                                                        + id));

                Long ownerStudentId =
                        result.getAssignment()
                                .getStudent()
                                .getStudentId();

                if (!ownerStudentId.equals(student.getStudentId())) {

                    throw new NotFoundException("Không tìm thấy AssessmentResult với ID = " + id);
                }

                return toResponse(result);
            }

            default -> throw new ForbiddenException(
                    "Không có quyền truy cập AssessmentResult");
        }
    }


    @Override
    public AssessmentResultResponse createResult(
            AssessmentResultCreateRequest request) {

        User currentUser = getCurrentUser();


        InternshipAssignment assignment =
                internshipAssignmentRepository.findById(request.getAssignmentId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy InternshipAssignment với ID = "
                                                + request.getAssignmentId()));


        Mentor mentor =
                mentorRepository.findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role MENTOR"));

        if (!assignment.getMentor().getMentorId()
                .equals(mentor.getMentorId())) {

            throw new NotFoundException(
                    "Không tìm thấy Assignment với ID = " + assignment.getAssignmentId());
        }

        if (assignment.getStatus() != InternshipAssignmentsStatus.IN_PROGRESS) {

            throw new BadRequestException(
                    "Không thể tạo AssessmentResult với Assignment ID = "
                            + assignment.getAssignmentId()
                            + " vì trạng thái hiện tại là "
                            + assignment.getStatus()
                            + ". Chỉ Assignment ở trạng thái IN_PROGRESS mới tạo được AssessmentResult");
        }

        AssessmentRound round =
                assessmentRoundRepository.findById(request.getRoundId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy AssessmentRound với ID = "
                                                + request.getRoundId()));

        if (!Boolean.TRUE.equals(round.getIsActive())) {

            throw new BadRequestException(
                    "AssessmentRound ID = "
                            + round.getRoundId()
                            + " hiện không hoạt động");
        }
        LocalDate now = LocalDate.now();

        if (now.isBefore(round.getStartDate())
                || now.isAfter(round.getEndDate())) {

            throw new BadRequestException(
                    "Hiện tại không nằm trong thời gian đánh giá của AssessmentRound ID = "
                            + round.getRoundId()
                            + " | startDate = "
                            + round.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " | endDate = "
                            + round.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        if (!assignment.getPhase().getPhaseId()
                .equals(round.getPhase().getPhaseId())) {

            throw new BadRequestException(
                    "AssessmentRound ID = "
                            + round.getRoundId()
                            + " không thuộc InternshipPhase của Assignment ID = "
                            + assignment.getAssignmentId());
        }

        EvaluationCriteria criterion =
                evaluationCriteriaRepository.findById(
                                request.getCriterionId()
                        )
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy EvaluationCriteria với ID = "
                                                + request.getCriterionId()));

        boolean existsRoundCriterion =
                roundCriteriaRepository
                        .existsByRound_RoundIdAndCriterion_CriterionId(
                                round.getRoundId(),
                                criterion.getCriterionId());

        if (!existsRoundCriterion) {

            throw new BadRequestException(
                    "Criterion ID = "
                            + criterion.getCriterionId()
                            + " không thuộc AssessmentRound ID = "
                            + round.getRoundId());
        }

        if (request.getScore()
                .compareTo(criterion.getMaxScore()) > 0) {

            throw new BadRequestException(
                    "Score = "
                            + request.getScore()
                            + " không được lớn hơn MaxScore = "
                            + criterion.getMaxScore()
                            + " của Criterion ID = "
                            + criterion.getCriterionId());
        }


        if (assessmentResultRepository
                .existsByAssignment_AssignmentIdAndRound_RoundIdAndCriterion_CriterionId(
                        assignment.getAssignmentId(),
                        round.getRoundId(),
                        criterion.getCriterionId())) {

            throw new ConflictException(
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

        assessmentResultRepository.save(result);

        return assessmentResultMapper.toResponse(result);
    }

    @Override
    public AssessmentResultResponse updateResult(
            Long id,
            AssessmentResultUpdateRequest request) {

        User currentUser = getCurrentUser();


        mentorRepository.findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role MENTOR"));

        AssessmentResult result =
                assessmentResultRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy AssessmentResult với ID = "
                                                + id));

        if (request.getComments() != null) {
            request.setComments(request.getComments().trim());
        }

        AssessmentRound round = result.getRound();

        if (!Boolean.TRUE.equals(round.getIsActive())) {

            throw new BadRequestException(
                    "AssessmentRound ID = "
                            + round.getRoundId()
                            + " hiện không hoạt động");
        }

        LocalDate now = LocalDate.now();

        if (now.isBefore(round.getStartDate())
                || now.isAfter(round.getEndDate())) {

            throw new BadRequestException(
                    "Hiện tại không nằm trong thời gian đánh giá của AssessmentRound ID = "
                            + round.getRoundId()
                            + " | startDate = "
                            + round.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " | endDate = "
                            + round.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        }

        if (request.getScore() != null
                && request.getScore()
                .compareTo(result.getCriterion().getMaxScore()) > 0) {

            throw new BadRequestException(
                    "Score = "
                            + request.getScore()
                            + " không được lớn hơn MaxScore = "
                            + result.getCriterion().getMaxScore()
                            + " của Criterion ID = "
                            + result.getCriterion().getCriterionId());
        }

        InternshipAssignmentsStatus status =
                result.getAssignment().getStatus();

        if (status != InternshipAssignmentsStatus.IN_PROGRESS) {

            throw new BadRequestException(
                    "Không thể cập nhật AssessmentResult"
                            + " vì Assignment hiện tại có trạng thái "
                            + status
                            + ". Chỉ Assignment ở trạng thái IN_PROGRESS mới được cập nhật");
        }

        if (!result.getEvaluatedBy().getUserId()
                .equals(currentUser.getUserId())) {

            throw new NotFoundException(
                    "Không tìm thấy AssessmentResult với ID = " + result.getResultId());
        }

        boolean hasChanges = false;

        if (request.getScore() != null
                && request.getScore().compareTo(result.getScore()) != 0) {

            hasChanges = true;
        }

        if (request.getComments() != null
                && !request.getComments()
                .equalsIgnoreCase(
                        result.getComments() == null
                                ? ""
                                : result.getComments())) {

            hasChanges = true;
        }

        if (!hasChanges) {
            return null;
        }

        assessmentResultMapper.updateEntityFromDto(request, result);

        result.setEvaluationDate(LocalDateTime.now());

        result.setUpdatedAt(LocalDateTime.now());

        assessmentResultRepository.save(result);

        return assessmentResultMapper.toResponse(result);
    }


    @Override
    public void deleteResult(Long id) {

        User currentUser = getCurrentUser();


        mentorRepository.findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role MENTOR"));


        AssessmentResult result =
                assessmentResultRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy AssessmentResult với ID = "
                                                + id));

        AssessmentRound round = result.getRound();

        if (!Boolean.TRUE.equals(round.getIsActive())) {

            throw new BadRequestException(
                    "AssessmentRound ID = "
                            + round.getRoundId()
                            + " hiện không hoạt động");
        }

        LocalDate now = LocalDate.now();

        if (now.isBefore(round.getStartDate())
                || now.isAfter(round.getEndDate())) {

            throw new BadRequestException(
                    "Hiện tại không nằm trong thời gian đánh giá của AssessmentRound ID = "
                            + round.getRoundId()
                            + " | startDate = "
                            + round.getStartDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + " | endDate = "
                            + round.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        InternshipAssignmentsStatus status =
                result.getAssignment().getStatus();

        if (status != InternshipAssignmentsStatus.IN_PROGRESS) {

            throw new BadRequestException(
                    "Không thể xóa AssessmentResult"
                            + " vì Assignment hiện tại có trạng thái "
                            + status
                            + ". Chỉ Assignment ở trạng thái IN_PROGRESS mới được xóa");
        }

        if (!result.getEvaluatedBy().getUserId()
                .equals(currentUser.getUserId())) {

            throw new NotFoundException(
                    "Không tìm thấy AssessmentResult với ID = " + result.getResultId());
        }

        assessmentResultRepository.delete(result);

    }


}



