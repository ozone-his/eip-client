package net.mekomsolutions.eip.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.openmrs.eip"})
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(final String[] args) {
		logger.info("Starting application . . .");
		
		SpringApplication.run(Application.class, args);
	}
	
}
