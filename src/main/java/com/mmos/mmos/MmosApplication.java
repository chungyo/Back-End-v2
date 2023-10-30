package com.mmos.mmos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MmosApplication {

//	@Autowired
//	private EmailService senderService;

	public static void main(String[] args) {
		SpringApplication.run(MmosApplication.class, args);
	}

//	@EventListener(ApplicationReadyEvent.class)
//	public void sendMail() {
//		senderService.sendEmail("guu119@naver.com",
//				"This is Subject",
//				"This is body of email");
//	}
}
