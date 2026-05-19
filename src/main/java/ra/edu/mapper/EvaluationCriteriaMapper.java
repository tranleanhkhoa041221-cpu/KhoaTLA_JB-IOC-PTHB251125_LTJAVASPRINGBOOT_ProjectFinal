package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.EvaluationCriteriaCreateRequest;
import ra.edu.dto.request.EvaluationCriteriaUpdateRequest;
import ra.edu.dto.response.EvaluationCriteriaResponse;
import ra.edu.entity.EvaluationCriteria;

@Mapper(componentModel = "spring")
public interface EvaluationCriteriaMapper {

    EvaluationCriteriaResponse toResponse(EvaluationCriteria criteria);

    @Mapping(target = "criterionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roundCriteria", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    EvaluationCriteria toEntity(EvaluationCriteriaCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "criterionId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "roundCriteria", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    void updateEntityFromDto(EvaluationCriteriaUpdateRequest request,
                             @MappingTarget EvaluationCriteria criteria);

}
