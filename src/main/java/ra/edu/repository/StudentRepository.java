package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.Student;

import java.time.LocalDate;


public interface StudentRepository extends JpaRepository<Student, Long> {

    boolean existsByStudentCode(String studentCode);

    boolean existsByUser_UserId(Long userId);

    Page<Student> findAllByMajorContainingIgnoreCase(String major, Pageable pageable);

    Page<Student> findAllByClassNameContainingIgnoreCase(String className, Pageable pageable);

    Page<Student> findAllByMajorContainingIgnoreCaseAndClassNameContainingIgnoreCase(String major, String className, Pageable pageable);

    Page<Student> findAllByStudentCodeContainingIgnoreCase(
            String studentCode,
            Pageable pageable
    );

    Page<Student> findAllByAddressContainingIgnoreCase(
            String address,
            Pageable pageable
    );

    Page<Student> findAllByDateOfBirth(
            LocalDate dateOfBirth,
            Pageable pageable
    );

    Page<Student> findAllByUser_UsernameContainingIgnoreCase(
            String username,
            Pageable pageable
    );


    Page<Student> findAllByUser_FullNameContainingIgnoreCase(
            String fullName,
            Pageable pageable
    );

    Page<Student> findAllByUser_EmailContainingIgnoreCase(
            String email,
            Pageable pageable
    );

    Page<Student> findAllByUser_PhoneNumberContainingIgnoreCase(
            String phoneNumber,
            Pageable pageable
    );



    Page<Student> findAllByAssignments_Mentor_MentorIdAndStudentCodeContainingIgnoreCase(
            Long mentorId,
            String studentCode,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndAddressContainingIgnoreCase(
            Long mentorId,
            String address,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndDateOfBirth(
            Long mentorId,
            LocalDate dateOfBirth,
            Pageable pageable
    );


    Page<Student> findAllByAssignments_Mentor_MentorIdAndUser_UsernameContainingIgnoreCase(
            Long mentorId,
            String username,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndUser_FullNameContainingIgnoreCase(
            Long mentorId,
            String fullName,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndUser_EmailContainingIgnoreCase(
            Long mentorId,
            String email,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndUser_PhoneNumberContainingIgnoreCase(
            Long mentorId,
            String phoneNumber,
            Pageable pageable
    );


    Page<Student> findAllByAssignments_Mentor_MentorId(
            Long mentorId,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndMajorContainingIgnoreCase(
            Long mentorId,
            String major,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndClassNameContainingIgnoreCase(
            Long mentorId,
            String className,
            Pageable pageable
    );

    Page<Student> findAllByAssignments_Mentor_MentorIdAndMajorContainingIgnoreCaseAndClassNameContainingIgnoreCase(
            Long mentorId,
            String major,
            String className,
            Pageable pageable
    );

    boolean existsByAssignments_Mentor_MentorIdAndStudentId(
            Long mentorId,
            Long studentId
    );


}
