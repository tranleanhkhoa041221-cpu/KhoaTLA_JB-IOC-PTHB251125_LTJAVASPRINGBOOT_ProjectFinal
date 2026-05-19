package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.InternshipAssignment;
import ra.edu.entity.InternshipAssignmentsStatus;

import java.time.LocalDateTime;

public interface InternshipAssignmentRepository extends JpaRepository<InternshipAssignment, Long> {

    boolean existsByStudent_StudentIdAndPhase_PhaseId(
            Long studentId,
            Long phaseId
    );

    Page<InternshipAssignment> findAllByStudent_StudentId(
            Long studentId,
            Pageable pageable
    );

    Page<InternshipAssignment> findAllByMentor_MentorId(
            Long mentorId,
            Pageable pageable
    );

    Page<InternshipAssignment> findAllByPhase_PhaseId(
            Long phaseId,
            Pageable pageable
    );

    Page<InternshipAssignment> findAllByStatus(
            InternshipAssignmentsStatus status,
            Pageable pageable
    );

    Page<InternshipAssignment> findAllByAssignedDate(
            LocalDateTime assignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment> findAllByAssignedDateBetween(
            LocalDateTime minAssignedDate,
            LocalDateTime maxAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment> findAllByAssignedDateGreaterThanEqual(
            LocalDateTime minAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment> findAllByAssignedDateLessThanEqual(
            LocalDateTime maxAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_User_FullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_User_EmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );


    Page<InternshipAssignment>
    findAllByStudent_User_UsernameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_User_PhoneNumberContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_User_FullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_User_EmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_User_UsernameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_User_PhoneNumberContainingIgnoreCase(
            String email,
            Pageable pageable
    );


    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndStudent_StudentId(
            Long mentorId,
            Long studentId,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndPhase_PhaseId(
            Long mentorId,
            Long phaseId,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndStatus(
            Long mentorId,
            InternshipAssignmentsStatus status,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndAssignedDate(
            Long mentorId,
            LocalDateTime assignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndAssignedDateBetween(
            Long mentorId,
            LocalDateTime minAssignedDate,
            LocalDateTime maxAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndAssignedDateGreaterThanEqual(
            Long mentorId,
            LocalDateTime minAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndAssignedDateLessThanEqual(
            Long mentorId,
            LocalDateTime maxAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndMentor_User_UsernameContainingIgnoreCase(
            Long mentorId,
            String username,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndMentor_User_FullNameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndMentor_User_EmailContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndMentor_User_PhoneNumberContainingIgnoreCase(
            Long mentorId,
            String phoneNumber,
            Pageable pageable
    );


    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndStudent_User_FullNameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndStudent_User_EmailContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndStudent_User_UsernameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByMentor_MentorIdAndStudent_User_PhoneNumberContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );


    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndMentor_MentorId(
            Long studentId,
            Long mentorId,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndPhase_PhaseId(
            Long studentId,
            Long phaseId,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndStatus(
            Long studentId,
            InternshipAssignmentsStatus status,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndAssignedDate(
            Long studentId,
            LocalDateTime assignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndAssignedDateBetween(
            Long studentId,
            LocalDateTime minAssignedDate,
            LocalDateTime maxAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndAssignedDateGreaterThanEqual(
            Long studentId,
            LocalDateTime minAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndAssignedDateLessThanEqual(
            Long studentId,
            LocalDateTime maxAssignedDate,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndStudent_User_UsernameContainingIgnoreCase(
            Long studentId,
            String username,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndStudent_User_FullNameContainingIgnoreCase(
            Long studentId,
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndStudent_User_EmailContainingIgnoreCase(
            Long studentId,
            String email,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndStudent_User_PhoneNumberContainingIgnoreCase(
            Long studentId,
            String phoneNumber,
            Pageable pageable
    );


    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndMentor_User_FullNameContainingIgnoreCase(
            Long studentId,
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndMentor_User_EmailContainingIgnoreCase(
            Long studentId,
            String email,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndMentor_User_UsernameContainingIgnoreCase(
            Long studentId,
            String fullName,
            Pageable pageable
    );

    Page<InternshipAssignment>
    findAllByStudent_StudentIdAndMentor_User_PhoneNumberContainingIgnoreCase(
            Long studentId,
            String email,
            Pageable pageable
    );
}