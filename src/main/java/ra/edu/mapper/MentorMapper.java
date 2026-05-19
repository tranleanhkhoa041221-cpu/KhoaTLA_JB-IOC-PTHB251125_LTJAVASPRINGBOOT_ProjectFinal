package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.MentorCreateRequest;
import ra.edu.dto.request.MentorUpdateRequest;
import ra.edu.dto.response.MentorResponse;
import ra.edu.entity.Mentor;

@Mapper(componentModel = "spring")
public interface MentorMapper {

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    MentorResponse toResponse(Mentor mentor);

    @Mapping(target = "mentorId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "internshipAssignments", ignore = true)
    Mentor toEntity(MentorCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "mentorId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "internshipAssignments", ignore = true)
    void updateEntityFromDto(MentorUpdateRequest request,
                             @MappingTarget Mentor mentor);
}

