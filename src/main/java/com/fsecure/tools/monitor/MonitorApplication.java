package com.fsecure.tools.monitor;

import org.slf4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.slf4j.LoggerFactory.getLogger;

@SpringBootApplication
public class MonitorApplication {

	private static final Logger logger = getLogger(MonitorApplication.class);

	public static void main(String[] args) {
		logger.info("Starting MonitorApp");
		SpringApplication.run(MonitorApplication.class, args);
	}

}
