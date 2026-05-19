package ra.edu.mapper;

import org.mapstruct.*;
import ra.edu.dto.request.InternshipAssignmentCreateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateRequest;
import ra.edu.dto.request.InternshipAssignmentUpdateStatusRequest;
import ra.edu.dto.response.InternshipAssignmentResponse;
import ra.edu.entity.InternshipAssignment;

@Mapper(componentModel = "spring")
public interface InternshipAssignmentMapper {

    @Mapping(target = "studentId", source = "student.studentId")
    @Mapping(target = "studentUsername", source = "student.user.username")
    @Mapping(target = "studentFullName", source = "student.user.fullName")
    @Mapping(target = "studentEmail", source = "student.user.email")
    @Mapping(target = "studentPhoneNumber", source = "student.user.phoneNumber")
    @Mapping(target = "mentorId", source = "mentor.mentorId")
    @Mapping(target = "mentorUsername", source = "mentor.user.username")
    @Mapping(target = "mentorFullName", source = "mentor.user.fullName")
    @Mapping(target = "mentorEmail", source = "mentor.user.email")
    @Mapping(target = "mentorPhoneNumber", source = "mentor.user.phoneNumber")
    @Mapping(target = "phaseId", source = "phase.phaseId")
    @Mapping(target = "phaseName", source = "phase.phaseName")
    InternshipAssignmentResponse toResponse(InternshipAssignment assignment);

    @Mapping(target = "assignmentId", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "assignedDate", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    InternshipAssignment toEntity(InternshipAssignmentCreateRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assignmentId", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    void updateEntityFromDto(InternshipAssignmentUpdateRequest request,
                             @MappingTarget InternshipAssignment assignment);

    @BeanMapping(nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "assignmentId", ignore = true)
    @Mapping(target = "student", ignore = true)
    @Mapping(target = "mentor", ignore = true)
    @Mapping(target = "phase", ignore = true)
    @Mapping(target = "assignedDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "assessmentResults", ignore = true)
    void updateStatusFromDto(InternshipAssignmentUpdateStatusRequest request,
                             @MappingTarget InternshipAssignment assignment);
}
