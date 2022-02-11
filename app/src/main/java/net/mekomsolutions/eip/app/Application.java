package net.mekomsolutions.eip.app;

import org.openmrs.eip.app.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication(scanBasePackages = {"org.openmrs.eip, net.mekomsolutions.eip.utils"})
@Import(AppConfig.class)
public class Application {
	
	private static final Logger logger = LoggerFactory.getLogger(Application.class);
	
	public static void main(final String[] args) {
		logger.info("Starting application . . .");
		
		SpringApplication.run(Application.class, args);
	}
	
}
