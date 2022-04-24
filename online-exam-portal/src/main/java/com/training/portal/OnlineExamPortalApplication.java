package com.training.portal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
public class OnlineExamPortalApplication {

	public static void main(String[] args) {
		SpringApplication.run(OnlineExamPortalApplication.class, args);
	}

	@GetMapping("/health-check")
	public String checkHealthStatus() {
		return "Onlin-exam-portal is healthy";
	}
}


