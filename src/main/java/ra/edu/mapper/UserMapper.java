package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.UserCreateRequest;
import ra.edu.dto.request.UserUpdateRequest;
import ra.edu.dto.response.UserResponse;
import ra.edu.entity.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    User toEntity(UserCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    void updateEntityFromDto(UserUpdateRequest request,
                             @MappingTarget User user);

}
