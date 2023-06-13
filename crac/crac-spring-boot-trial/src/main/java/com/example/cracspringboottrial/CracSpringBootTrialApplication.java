package com.example.cracspringboottrial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CracSpringBootTrialApplication {

	public static void main(String[] args) {
		SpringApplication.run(CracSpringBootTrialApplication.class, args);
	}

    @Bean
    public Counter counter() {
        return new Counter();
    }
}
