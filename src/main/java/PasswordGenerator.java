import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("Admin:   " + encoder.encode("123456"));
        System.out.println("Mentor:  " + encoder.encode("123456"));
        System.out.println("Student: " + encoder.encode("123456"));
    }
}
