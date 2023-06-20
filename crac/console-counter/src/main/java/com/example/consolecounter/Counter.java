package com.example.consolecounter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Counter {
    private int count = 0;

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
    }
}
