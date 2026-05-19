package ra.edu.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ra.edu.entity.User;
import ra.edu.entity.UserRole;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Optional<User> findByPhoneNumber(String phoneNumber);

    Page<User> findAllByRole(UserRole role, Pageable pageable);

    Page<User> findAllByUsernameContainingIgnoreCase(String username, Pageable pageable);

    Page<User> findAllByFullNameContainingIgnoreCase(String fullName, Pageable pageable);

    Page<User> findAllByEmailContainingIgnoreCase(String email, Pageable pageable);

    Page<User> findAllByPhoneNumberContainingIgnoreCase(String phoneNumber, Pageable pageable);

    Page<User> findAllByIsActive(Boolean isActive, Pageable pageable);

}
