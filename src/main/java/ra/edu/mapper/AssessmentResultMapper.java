package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.AssessmentResultCreateRequest;
import ra.edu.dto.request.AssessmentResultUpdateRequest;
import ra.edu.dto.response.AssessmentResultResponse;
import ra.edu.entity.AssessmentResult;

@Mapper(componentModel = "spring")
public interface AssessmentResultMapper {

    @Mapping(target = "assignmentId", source = "assignment.assignmentId")
    @Mapping(target = "studentId", source = "assignment.student.studentId")
    @Mapping(target = "studentUsername", source = "assignment.student.user.username")
    @Mapping(target = "studentFullName", source = "assignment.student.user.fullName")
    @Mapping(target = "studentEmail", source = "assignment.student.user.email")
    @Mapping(target = "studentPhoneNumber", source = "assignment.student.user.phoneNumber")
    @Mapping(target = "mentorId", source = "assignment.mentor.mentorId")
    @Mapping(target = "mentorUsername", source = "assignment.mentor.user.username")
    @Mapping(target = "mentorFullName", source = "assignment.mentor.user.fullName")
    @Mapping(target = "mentorEmail", source = "assignment.mentor.user.email")
    @Mapping(target = "mentorPhoneNumber", source = "assignment.mentor.user.phoneNumber")
    @Mapping(target = "phaseId", source = "assignment.phase.phaseId")
    @Mapping(target = "phaseName", source = "assignment.phase.phaseName")
    @Mapping(target = "assignmentStatus", source = "assignment.status")
    @Mapping(target = "roundId", source = "round.roundId")
    @Mapping(target = "roundName", source = "round.roundName")
    @Mapping(target = "criterionId", source = "criterion.criterionId")
    @Mapping(target = "criterionName", source = "criterion.criterionName")
    @Mapping(target = "evaluatedById", source = "evaluatedBy.userId")
    @Mapping(target = "evaluatedByUsername", source = "evaluatedBy.username")
    @Mapping(target = "evaluatedByFullName", source = "evaluatedBy.fullName")
    @Mapping(target = "evaluatedByEmail", source = "evaluatedBy.email")
    @Mapping(target = "evaluatedByPhoneNumber", source = "evaluatedBy.phoneNumber")
    AssessmentResultResponse toResponse(AssessmentResult assessmentResult);

    @Mapping(target = "resultId", ignore = true)
    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "criterion", ignore = true)
    @Mapping(target = "evaluatedBy", ignore = true)
    @Mapping(target = "evaluationDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    AssessmentResult toEntity(AssessmentResultCreateRequest request);


    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "resultId", ignore = true)
    @Mapping(target = "assignment", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "criterion", ignore = true)
    @Mapping(target = "evaluatedBy", ignore = true)
    @Mapping(target = "evaluationDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(AssessmentResultUpdateRequest request,
                             @MappingTarget AssessmentResult assessmentResult
    );
}
