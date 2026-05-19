package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.entity.InternshipPhase;

@Mapper(componentModel = "spring")
public interface InternshipPhaseMapper {

    InternshipPhaseResponse toResponse(InternshipPhase phase);

    @Mapping(target = "phaseId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "internshipAssignments", ignore = true)
    @Mapping(target = "assessmentRounds", ignore = true)
    InternshipPhase toEntity(InternshipPhaseCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phaseId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "internshipAssignments", ignore = true)
    @Mapping(target = "assessmentRounds", ignore = true)
    void updateEntityFromDto(InternshipPhaseUpdateRequest request,
                             @MappingTarget InternshipPhase phase);
}
