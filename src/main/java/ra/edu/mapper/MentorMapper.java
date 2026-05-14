package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;
import ra.edu.entity.Mentor;

@Mapper(componentModel = "spring")
public interface MentorMapper {
    // Map từ entity sang response
    @Mapping(source = "user.username", target = "username")
    @Mapping(source = "user.fullName", target = "fullName")
    @Mapping(source = "user.email", target = "email")
    @Mapping(source = "user.phoneNumber", target = "phoneNumber")
    MentorResponse toResponse(Mentor mentor);

    // Map từ request tạo mới sang entity
    @Mapping(target = "mentorId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    Mentor toEntity(MentorCreateRequest request);

    // Map từ request update sang entity (update in-place)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "mentorId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assignments", ignore = true)
    void updateEntityFromDto(MentorUpdateRequest request, @MappingTarget Mentor mentor);
}

