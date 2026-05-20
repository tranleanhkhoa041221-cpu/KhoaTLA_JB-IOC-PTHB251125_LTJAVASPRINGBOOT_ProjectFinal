package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.Mentor;

import java.util.Optional;

public interface MentorRepository extends JpaRepository<Mentor, Long> {

    boolean existsByUser_UserId(Long userId);

    Optional<Mentor> findByUser_UserId(Long userId);

    Page<Mentor> findAllByDepartmentContainingIgnoreCase(String department, Pageable pageable);

    Page<Mentor> findAllByAcademicRankContainingIgnoreCase(String academicRank, Pageable pageable);

    Page<Mentor> findAllByDepartmentContainingIgnoreCaseAndAcademicRankContainingIgnoreCase(String department, String academicRank, Pageable pageable);

    Page<Mentor> findAllByUser_UsernameContainingIgnoreCase(
            String username,
            Pageable pageable
    );


    Page<Mentor> findAllByUser_FullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<Mentor> findAllByUser_EmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<Mentor> findAllByUser_PhoneNumberContainingIgnoreCase(
            String phoneNumber,
            Pageable pageable
    );


    Page<Mentor> findAllByInternshipAssignments_Student_StudentIdAndUser_UsernameContainingIgnoreCase(
            Long mentorId,
            String username,
            Pageable pageable
    );

    Page<Mentor> findAllByInternshipAssignments_Student_StudentIdAndUser_FullNameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<Mentor> findAllByInternshipAssignments_Student_StudentIdAndUser_EmailContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );

    Page<Mentor> findAllByInternshipAssignments_Student_StudentIdAndUser_PhoneNumberContainingIgnoreCase(
            Long mentorId,
            String phoneNumber,
            Pageable pageable
    );



    Page<Mentor> findAllByInternshipAssignments_Student_StudentId(
            Long studentId,
            Pageable pageable
    );

    Page<Mentor> findAllByInternshipAssignments_Student_StudentIdAndDepartmentContainingIgnoreCase(
            Long studentId,
            String department,
            Pageable pageable
    );

    Page<Mentor> findAllByInternshipAssignments_Student_StudentIdAndAcademicRankContainingIgnoreCase(
            Long studentId,
            String academicRank,
            Pageable pageable
    );

    Page<Mentor> findAllByInternshipAssignments_Student_StudentIdAndDepartmentContainingIgnoreCaseAndAcademicRankContainingIgnoreCase(
            Long studentId,
            String department,
            String academicRank,
            Pageable pageable
    );


    boolean existsByInternshipAssignments_Student_StudentIdAndMentorId(
            Long studentId,
            Long mentorId
    );

}
