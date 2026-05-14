package ra.edu;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class ProjectspringbootApplication {
    @PostConstruct
    public void init() {
        // Ép toàn bộ JVM sử dụng múi giờ GMT+7
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));
    }

	public static void main(String[] args) {
		SpringApplication.run(ProjectspringbootApplication.class, args);
	}

}
