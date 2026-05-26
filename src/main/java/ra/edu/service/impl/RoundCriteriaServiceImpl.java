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
import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaFilterRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.PaginationResponse;
import ra.edu.dto.response.RoundCriteriaResponse;
import ra.edu.entity.*;
import ra.edu.exception.BadRequestException;
import ra.edu.exception.ConflictException;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
import ra.edu.helper.AccessValidator;
import ra.edu.mapper.RoundCriteriaMapper;
import ra.edu.repository.*;
import ra.edu.service.RoundCriteriaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoundCriteriaServiceImpl implements RoundCriteriaService {

    private final RoundCriteriaRepository roundCriteriaRepository;

    private final AssessmentRoundRepository assessmentRoundRepository;

    private final EvaluationCriteriaRepository evaluationCriteriaRepository;

    private final RoundCriteriaMapper roundCriteriaMapper;

    private final AssessmentResultRepository assessmentResultRepository;

    private final MentorRepository mentorRepository;

    private final StudentRepository studentRepository;

    private final InternshipAssignmentRepository internshipAssignmentRepository;

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal principal =
                (UserPrincipal) authentication.getPrincipal();

        return principal.getUser();
    }

    private void validateFilter(
            RoundCriteriaFilterRequest request) {

        if (request.getMinWeight() != null
                && request.getMaxWeight() != null
                && request.getMinWeight()
                .compareTo(request.getMaxWeight()) > 0) {

            throw new BadRequestException(
                    "minWeight không được lớn hơn maxWeight");
        }

        if (request.getRoundId() != null
                && !assessmentRoundRepository.existsById(
                request.getRoundId())) {

            throw new NotFoundException(
                    "Không tìm thấy đợt đánh giá với ID = "
                            + request.getRoundId());
        }

        if (request.getCriterionId() != null
                && !evaluationCriteriaRepository.existsById(
                request.getCriterionId())) {

            throw new NotFoundException(
                    "Không tìm thấy tiêu chí đánh giá với ID = "
                            + request.getCriterionId());
        }
    }

    private Page<RoundCriteria> getAllForAdmin(
            RoundCriteriaFilterRequest request,
            Pageable pageable) {

        if (request.getRoundId() != null
                && request.getCriterionId() != null) {

            return roundCriteriaRepository
                    .findAllByRound_RoundIdAndCriterion_CriterionId(
                            request.getRoundId(),
                            request.getCriterionId(),
                            pageable);
        } else if (request.getRoundId() != null) {

            return roundCriteriaRepository
                    .findAllByRound_RoundId(
                            request.getRoundId(),
                            pageable);
        } else if (request.getCriterionId() != null) {

            return roundCriteriaRepository
                    .findAllByCriterion_CriterionId(
                            request.getCriterionId(),
                            pageable);
        } else if (request.getRoundName() != null
                && !request.getRoundName().isBlank()) {

            return roundCriteriaRepository
                    .findAllByRound_RoundNameContainingIgnoreCase(
                            request.getRoundName(),
                            pageable);
        } else if (request.getCriterionName() != null
                && !request.getCriterionName().isBlank()) {

            return roundCriteriaRepository
                    .findAllByCriterion_CriterionNameContainingIgnoreCase(
                            request.getCriterionName(),
                            pageable);
        } else if (request.getWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeight(
                            request.getWeight(),
                            pageable);
        } else if (request.getMinWeight() != null
                && request.getMaxWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightBetween(
                            request.getMinWeight(),
                            request.getMaxWeight(),
                            pageable);
        } else if (request.getMinWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightGreaterThanEqual(
                            request.getMinWeight(),
                            pageable);
        } else if (request.getMaxWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightLessThanEqual(
                            request.getMaxWeight(),
                            pageable);
        }

        return roundCriteriaRepository.findAll(pageable);
    }

    private Page<RoundCriteria> getAllForMentor(
            RoundCriteriaFilterRequest request,
            Pageable pageable,
            User currentUser) {

        Mentor mentor = mentorRepository
                .findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role MENTOR"));

        Long mentorId = mentor.getMentorId();

        if (request.getRoundId() != null
                && request.getCriterionId() != null) {

            return roundCriteriaRepository
                    .findAllByRound_RoundIdAndCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getRoundId(),
                            request.getCriterionId(),
                            mentorId,
                            pageable);
        } else if (request.getRoundId() != null) {

            return roundCriteriaRepository
                    .findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getRoundId(),
                            mentorId,
                            pageable);
        } else if (request.getCriterionId() != null) {

            return roundCriteriaRepository
                    .findAllByCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getCriterionId(),
                            mentorId,
                            pageable);
        } else if (request.getRoundName() != null
                && !request.getRoundName().isBlank()) {

            return roundCriteriaRepository
                    .findAllByRound_RoundNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getRoundName(),
                            mentorId,
                            pageable);
        } else if (request.getCriterionName() != null
                && !request.getCriterionName().isBlank()) {

            return roundCriteriaRepository
                    .findAllByCriterion_CriterionNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getCriterionName(),
                            mentorId,
                            pageable);
        } else if (request.getWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getWeight(),
                            mentorId,
                            pageable);
        } else if (request.getMinWeight() != null
                && request.getMaxWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightBetweenAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getMinWeight(),
                            request.getMaxWeight(),
                            mentorId,
                            pageable);
        } else if (request.getMinWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightGreaterThanEqualAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getMinWeight(),
                            mentorId,
                            pageable);
        } else if (request.getMaxWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightLessThanEqualAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                            request.getMaxWeight(),
                            mentorId,
                            pageable);
        }

        return roundCriteriaRepository
                .findAllByRound_Phase_InternshipAssignments_Mentor_MentorId(
                        mentorId,
                        pageable);
    }

    private Page<RoundCriteria> getAllForStudent(
            RoundCriteriaFilterRequest request,
            Pageable pageable,
            User currentUser) {

        Student student = studentRepository
                .findByUser_UserId(currentUser.getUserId())
                .orElseThrow(() ->
                        new NotFoundException(
                                "User ID = "
                                        + currentUser.getUserId()
                                        + " chưa được liên kết với role STUDENT"));

        Long studentId = student.getStudentId();

        if (request.getRoundId() != null
                && request.getCriterionId() != null) {

            return roundCriteriaRepository
                    .findAllByRound_RoundIdAndCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getRoundId(),
                            request.getCriterionId(),
                            studentId,
                            pageable);
        } else if (request.getRoundId() != null) {

            return roundCriteriaRepository
                    .findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getRoundId(),
                            studentId,
                            pageable);
        } else if (request.getCriterionId() != null) {

            return roundCriteriaRepository
                    .findAllByCriterion_CriterionIdAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getCriterionId(),
                            studentId,
                            pageable);
        } else if (request.getRoundName() != null
                && !request.getRoundName().isBlank()) {

            return roundCriteriaRepository
                    .findAllByRound_RoundNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getRoundName(),
                            studentId,
                            pageable);
        } else if (request.getCriterionName() != null
                && !request.getCriterionName().isBlank()) {

            return roundCriteriaRepository
                    .findAllByCriterion_CriterionNameContainingIgnoreCaseAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getCriterionName(),
                            studentId,
                            pageable);
        } else if (request.getWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getWeight(),
                            studentId,
                            pageable);
        } else if (request.getMinWeight() != null
                && request.getMaxWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightBetweenAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getMinWeight(),
                            request.getMaxWeight(),
                            studentId,
                            pageable);
        } else if (request.getMinWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightGreaterThanEqualAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getMinWeight(),
                            studentId,
                            pageable);
        } else if (request.getMaxWeight() != null) {

            return roundCriteriaRepository
                    .findAllByWeightLessThanEqualAndRound_Phase_InternshipAssignments_Student_StudentId(
                            request.getMaxWeight(),
                            studentId,
                            pageable);
        }

        return roundCriteriaRepository
                .findAllByRound_Phase_InternshipAssignments_Student_StudentId(
                        studentId,
                        pageable);
    }

    @Override
    public PaginationResponse<RoundCriteriaResponse> getAllRoundCriteria(
            RoundCriteriaFilterRequest request) {

        User currentUser = getCurrentUser();

        validateFilter(request);

        Pageable pageable = PageRequest.of(
                request.getPage() - 1,
                request.getSize(),
                Sort.by("roundCriterionId").descending());

        Page<RoundCriteria> roundCriteriaPage;

        switch (currentUser.getRole()) {

            case ADMIN -> roundCriteriaPage =
                    getAllForAdmin(
                            request,
                            pageable);

            case MENTOR -> roundCriteriaPage =
                    getAllForMentor(
                            request,
                            pageable,
                            currentUser);

            case STUDENT -> roundCriteriaPage =
                    getAllForStudent(
                            request,
                            pageable,
                            currentUser);

            default -> throw new ForbiddenException(
                    "Không được phép truy cập");
        }


        List<RoundCriteriaResponse> items =
                roundCriteriaPage.getContent()
                        .stream()
                        .map(roundCriteriaMapper::toResponse)
                        .toList();

        Pagination pagination = Pagination.builder()
                .currentPage(request.getPage())
                .pageSize(request.getSize())
                .totalPages(roundCriteriaPage.getTotalPages())
                .totalItems(roundCriteriaPage.getTotalElements())
                .build();

        return PaginationResponse
                .<RoundCriteriaResponse>builder()
                .items(items)
                .pagination(pagination)
                .build();
    }

    @Override
    public List<RoundCriteriaResponse> getCriteriaByRoundId(
            Long roundId) {

        User currentUser = getCurrentUser();

        AssessmentRound round =
                assessmentRoundRepository.findById(roundId)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy đợt đánh giá với ID = "
                                                + roundId));

        List<RoundCriteria> roundCriteriaList;

        switch (currentUser.getRole()) {

            case ADMIN -> roundCriteriaList =
                    roundCriteriaRepository
                            .findAllByRound_RoundId(
                                    roundId,
                                    Sort.by("roundCriterionId")
                                            .ascending());

            case MENTOR -> {

                Mentor mentor = mentorRepository
                        .findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role MENTOR"));

                boolean hasAccess =
                        internshipAssignmentRepository
                                .existsByPhase_PhaseIdAndMentor_MentorId(
                                        round.getPhase().getPhaseId(),
                                        mentor.getMentorId());

                if (!hasAccess) {

                    throw new NotFoundException(
                            "Không tìm thấy đợt đánh giá với ID = "
                                    + roundId);
                }

                roundCriteriaList =
                        roundCriteriaRepository
                                .findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                                        roundId,
                                        mentor.getMentorId(),
                                        Sort.by("roundCriterionId")
                                                .ascending());
            }

            case STUDENT -> {

                Student student = studentRepository
                        .findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role STUDENT"));


                boolean hasAccess =
                        internshipAssignmentRepository
                                .existsByPhase_PhaseIdAndStudent_StudentId(
                                        round.getPhase().getPhaseId(),
                                        student.getStudentId());

                if (!hasAccess) {

                    throw new NotFoundException(
                            "Không tìm thấy đợt đánh giá với ID = "
                                    + roundId);
                }

                roundCriteriaList =
                        roundCriteriaRepository
                                .findAllByRound_RoundIdAndRound_Phase_InternshipAssignments_Student_StudentId(
                                        roundId,
                                        student.getStudentId(),
                                        Sort.by("roundCriterionId")
                                                .ascending());
            }

            default -> throw new BadRequestException(
                    "Role không hợp lệ");
        }

        if (roundCriteriaList.isEmpty()) {

            throw new NotFoundException(
                    "Không tìm thấy đợt đánh giá với ID = "
                            + roundId);
        }

        return roundCriteriaList.stream()
                .map(roundCriteriaMapper::toResponse)
                .toList();
    }

    @Override
    public RoundCriteriaResponse getRoundCriteriaById(Long id) {

        RoundCriteria roundCriteria =
                roundCriteriaRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException(
                                "Không tìm thấy tiêu chí trong đợt đánh giá với ID = " + id));

        User currentUser = getCurrentUser();

        switch (currentUser.getRole()) {

            case ADMIN -> {
                return roundCriteriaMapper.toResponse(roundCriteria);
            }

            case MENTOR -> {

                Mentor mentor = mentorRepository
                        .findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role MENTOR"));

                boolean exists =
                        roundCriteriaRepository
                                .existsByRoundCriterionIdAndRound_Phase_InternshipAssignments_Mentor_MentorId(
                                        id,
                                        mentor.getMentorId());

                if (!exists) {

                    throw new NotFoundException(
                            "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                    + id);
                }

                return roundCriteriaMapper.toResponse(roundCriteria);
            }

            case STUDENT -> {

                Student student = studentRepository
                        .findByUser_UserId(currentUser.getUserId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "User ID = "
                                                + currentUser.getUserId()
                                                + " chưa được liên kết với role STUDENT"));

                boolean exists =
                        roundCriteriaRepository
                                .existsByRoundCriterionIdAndRound_Phase_InternshipAssignments_Student_StudentId(
                                        id,
                                        student.getStudentId());

                if (!exists) {

                    throw new NotFoundException(
                            "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                    + id);
                }

                return roundCriteriaMapper.toResponse(roundCriteria);
            }

            default -> throw new ForbiddenException("Không có quyền truy cập");
        }

    }

    @Override
    public RoundCriteriaResponse createRoundCriteria(
            RoundCriteriaCreateRequest request) {

        AssessmentRound round =
                assessmentRoundRepository.findById(request.getRoundId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy đợt đánh giá với ID = "
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

        EvaluationCriteria criterion =
                evaluationCriteriaRepository.findById(
                                request.getCriterionId())
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí đánh giá với ID = "
                                                + request.getCriterionId()));

        if (roundCriteriaRepository
                .existsByRound_RoundIdAndCriterion_CriterionId(
                        request.getRoundId(),
                        request.getCriterionId())) {

            throw new ConflictException(
                    "Tiêu chí đánh giá ID = "
                            + request.getCriterionId()
                            + " đã tồn tại trong đợt đánh giá ID = "
                            + request.getRoundId());
        }

        BigDecimal currentTotalWeight =
                roundCriteriaRepository
                        .sumWeightByRound_RoundId(
                                request.getRoundId());

        if (currentTotalWeight == null) {
            currentTotalWeight = BigDecimal.ZERO;
        }

        BigDecimal newTotalWeight =
                currentTotalWeight.add(
                        request.getWeight());

        if (newTotalWeight.compareTo(BigDecimal.ONE) > 0) {

            throw new BadRequestException(
                    "Tổng weight của đợt đánh giá không được lớn hơn 1");
        }

        RoundCriteria roundCriteria = roundCriteriaMapper.toEntity(request);

        roundCriteria.setRound(round);

        roundCriteria.setCriterion(criterion);

        roundCriteria.setCreatedAt(LocalDateTime.now());

        roundCriteriaRepository.save(roundCriteria);

        return roundCriteriaMapper.toResponse(roundCriteria);
    }

    @Override
    public RoundCriteriaResponse updateRoundCriteria(
            Long id,
            RoundCriteriaUpdateRequest request) {

        RoundCriteria roundCriteria =
                roundCriteriaRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                + id));

        AssessmentRound round = roundCriteria.getRound();

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

        boolean hasChanges = false;

        boolean hasResult =
                assessmentResultRepository.existsByRound_RoundIdAndCriterion_CriterionId(
                        roundCriteria.getRound().getRoundId(),
                        roundCriteria.getCriterion().getCriterionId());

        if (hasResult) {
            throw new ConflictException(
                    "Không thể cập nhật weight vì đã có kết quả đánh giá");
        }


        BigDecimal currentTotalWeight =
                roundCriteriaRepository
                        .sumWeightByRound_RoundId(
                                roundCriteria.getRound().getRoundId());

        if (currentTotalWeight == null) {

            currentTotalWeight = BigDecimal.ZERO;
        }
        if (request.getWeight() != null) {

            BigDecimal newTotalWeight =
                    currentTotalWeight
                            .subtract(roundCriteria.getWeight())
                            .add(request.getWeight());

            if (newTotalWeight.compareTo(BigDecimal.ONE) > 0) {

                throw new BadRequestException(
                        "Tổng weight của đợt đánh giá không được lớn hơn 1");
            }

            if (request.getWeight().compareTo(roundCriteria.getWeight()) != 0) {
                hasChanges = true;
            }
        }

        if (!hasChanges) {
            return null;
        }

        roundCriteriaMapper.updateEntityFromDto(request, roundCriteria);

        roundCriteria.setUpdatedAt(LocalDateTime.now());

        roundCriteriaRepository.save(roundCriteria);

        return roundCriteriaMapper.toResponse(roundCriteria);
    }


    @Override
    public void deleteRoundCriteria(Long id) {

        RoundCriteria roundCriteria =
                roundCriteriaRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Không tìm thấy tiêu chí trong đợt đánh giá với ID = "
                                                + id));

        AssessmentRound round = roundCriteria.getRound();

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

        boolean hasResult =
                assessmentResultRepository.existsByRound_RoundIdAndCriterion_CriterionId(
                        roundCriteria.getRound().getRoundId(),
                        roundCriteria.getCriterion().getCriterionId());

        if (hasResult) {
            throw new ConflictException(
                    "Không thể xóa tiêu chí trong đợt đánh giá vì đã có kết quả đánh giá");
        }

        roundCriteriaRepository.delete(roundCriteria);

    }

}
