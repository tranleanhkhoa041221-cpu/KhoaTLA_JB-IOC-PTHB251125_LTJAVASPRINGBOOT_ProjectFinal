package ra.edu.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class AssessmentRoundFilterRequest {

    private int page = 1;

    private int size = 10;

    private String roundName;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate endDate;

    private String description;

    private Long phaseId;

    private String phaseName;

    private String isActive;
}
