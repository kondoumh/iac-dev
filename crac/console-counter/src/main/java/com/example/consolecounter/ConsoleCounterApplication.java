package com.example.consolecounter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ConsoleCounterApplication {

	public static void main(String[] args) {
		var context = SpringApplication.run(ConsoleCounterApplication.class, args);
        var counter = context.getBean(Counter.class);
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                break;
            }
            counter.increment();
            System.out.println("Count: " + counter.getCount());
        }
	}

    @Bean
    public Counter counter() {
        return new Counter();
    }}
