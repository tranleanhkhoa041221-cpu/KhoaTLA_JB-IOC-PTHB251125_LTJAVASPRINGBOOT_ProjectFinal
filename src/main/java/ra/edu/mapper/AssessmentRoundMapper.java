package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.AssessmentRoundCreateRequest;
import ra.edu.dto.request.AssessmentRoundUpdateRequest;
import ra.edu.dto.response.AssessmentRoundResponse;
import ra.edu.entity.AssessmentRound;

@Mapper(componentModel = "spring")
public interface AssessmentRoundMapper {

    @Mapping(target = "phaseId", source = "phase.phaseId")
    @Mapping(target = "phaseName", source = "phase.phaseName")
    AssessmentRoundResponse toResponse(AssessmentRound round);

    @Mapping(target = "roundId", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roundCriteria", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    AssessmentRound toEntity(AssessmentRoundCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roundId", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roundCriteria", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    void updateEntityFromDto(AssessmentRoundUpdateRequest request,
                             @MappingTarget AssessmentRound round);

}
