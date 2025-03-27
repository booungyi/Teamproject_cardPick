package team.cardpick_project.cardpick;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class CardpickApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardpickApplication.class, args);
	}

}
