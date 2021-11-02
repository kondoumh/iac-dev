package com.example.demo;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	private static final Logger logger = LoggerFactory.getLogger(DemoApplication.class);
	public static void main(String[] args) {
		var now = OffsetDateTime.now(ZoneId.systemDefault()).truncatedTo(ChronoUnit.SECONDS);
		var now2 = OffsetDateTime.now().truncatedTo(ChronoUnit.SECONDS);
		logger.info("now1: {}", now.toString());
		logger.info("now2: {}", now2.toString());
		SpringApplication.run(DemoApplication.class, args);
	}

}
