package ra.edu.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ra.edu.entity.User;
import ra.edu.exception.ForbiddenException;
import ra.edu.exception.NotFoundException;
import ra.edu.repository.MentorRepository;
import ra.edu.repository.StudentRepository;


@Component
@RequiredArgsConstructor
public class AccessValidator {

    private final MentorRepository mentorRepository;

    private final StudentRepository studentRepository;

    public void validateAccess(User user) {

        switch (user.getRole()) {

            case ADMIN -> {
            }

            case MENTOR -> mentorRepository
                    .findByUser_UserId(user.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            "User ID = " + user.getUserId()
                                    + " chưa được liên kết với role MENTOR"));

            case STUDENT -> studentRepository
                    .findByUser_UserId(user.getUserId())
                    .orElseThrow(() -> new NotFoundException(
                            "User ID = " + user.getUserId()
                                    + " chưa được liên kết với role STUDENT"));

            default -> throw new ForbiddenException(
                    "Không có quyền truy cập");
        }
    }
}
