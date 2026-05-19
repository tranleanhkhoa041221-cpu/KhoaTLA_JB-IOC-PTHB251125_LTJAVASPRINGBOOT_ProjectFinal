package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.RoundCriteriaCreateRequest;
import ra.edu.dto.request.RoundCriteriaUpdateRequest;
import ra.edu.dto.response.RoundCriteriaResponse;
import ra.edu.entity.RoundCriteria;

@Mapper(componentModel = "spring")
public interface RoundCriteriaMapper {

    @Mapping(target = "roundId", source = "round.roundId")
    @Mapping(target = "roundName", source = "round.roundName")
    @Mapping(target = "criterionId", source = "criterion.criterionId")
    @Mapping(target = "criterionName", source = "criterion.criterionName")
    RoundCriteriaResponse toResponse(RoundCriteria entity);

    @Mapping(target = "roundCriterionId", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "criterion", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RoundCriteria toEntity(RoundCriteriaCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roundCriterionId", ignore = true)
    @Mapping(target = "round", ignore = true)
    @Mapping(target = "criterion", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(RoundCriteriaUpdateRequest request,
                             @MappingTarget RoundCriteria entity);

}
