package ra.edu.dto.request;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
public class AssessmentResultFilterRequest {

    private int page = 1;

    private int size = 10;

    private Long assignmentId;
    private Long studentId;
    private Long mentorId;
    private Long phaseId;
    private InternshipAssignmentsStatus assignmentStatus;
    private Long roundId;
    private Long criterionId;
    private Long evaluatedById;

    private String studentUsername;
    private String studentFullName;
    private String studentEmail;
    private String studentPhoneNumber;

    private String mentorUsername;
    private String mentorFullName;
    private String mentorEmail;
    private String mentorPhoneNumber;

    private String phaseName;
    private String roundName;
    private String criterionName;

    private String evaluatedByUsername;
    private String evaluatedByFullName;
    private String evaluatedByEmail;
    private String evaluatedByPhoneNumber;

    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Score phải lớn hơn hoặc bằng 0"
    )
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "Score phải nhỏ hơn hoặc bằng 10"
    )
    private BigDecimal score;

    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "minScore phải lớn hơn hoặc bằng 0"
    )
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "minScore phải nhỏ hơn hoặc bằng 10"
    )
    private BigDecimal minScore;

    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "maxScore phải lớn hơn hoặc bằng 0"
    )
    @DecimalMax(
            value = "10.0",
            inclusive = true,
            message = "maxScore phải nhỏ hơn hoặc bằng 10"
    )
    private BigDecimal maxScore;

    private String comments;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime evaluationDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime minEvaluationDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime maxEvaluationDate;
}
