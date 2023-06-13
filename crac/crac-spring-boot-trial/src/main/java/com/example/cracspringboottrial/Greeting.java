package com.example.cracspringboottrial;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Greeting {

    private final Counter counter;

    public Greeting(Counter counter) {
        this.counter = counter;
    }

    @GetMapping("/")
    public String hello() {
        counter.increment();
        return "Hello! You are visitor number " + counter.getCount() + ".\n";
    }
}
