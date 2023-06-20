package com.example.consolecounter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public class Counter {
    private int count = 0;

    @Value("${deploy.stage}")
    private String deployStage;

    public String getDeployStage() {
        return deployStage;
    }

    public int getCount() {
        return count;
    }

    public void increment() {
        count++;
    }
}
