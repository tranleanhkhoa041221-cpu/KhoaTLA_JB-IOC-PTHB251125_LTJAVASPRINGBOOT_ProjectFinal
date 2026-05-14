package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.InternshipPhaseCreateRequest;
import ra.edu.dto.request.InternshipPhaseUpdateRequest;
import ra.edu.dto.response.InternshipPhaseResponse;
import ra.edu.entity.InternshipPhase;

@Mapper(componentModel = "spring")
public interface InternshipPhaseMapper {
    // Map từ entity sang response
    InternshipPhaseResponse toResponse(InternshipPhase phase);

    // Map từ request tạo mới sang entity
    @Mapping(target = "phaseId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "assessmentRounds", ignore = true)
    InternshipPhase toEntity(InternshipPhaseCreateRequest request);

    // Map từ request update sang entity (update in-place)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "phaseId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    @Mapping(target = "assessmentRounds", ignore = true)
    void updateEntityFromDto(InternshipPhaseUpdateRequest request, @MappingTarget InternshipPhase phase);
}
