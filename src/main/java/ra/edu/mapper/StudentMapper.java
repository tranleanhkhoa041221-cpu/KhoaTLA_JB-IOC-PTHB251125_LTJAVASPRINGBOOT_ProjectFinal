package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.StudentCreateRequest;
import ra.edu.dto.request.StudentUpdateRequest;
import ra.edu.dto.response.StudentResponse;
import ra.edu.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "fullName", source = "user.fullName")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    StudentResponse toResponse(Student student);

    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "internshipAssignments", ignore = true)
    Student toEntity(StudentCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "studentId", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "studentCode", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "internshipAssignments", ignore = true)
    void updateEntityFromDto(StudentUpdateRequest request,
                             @MappingTarget Student student);
}

