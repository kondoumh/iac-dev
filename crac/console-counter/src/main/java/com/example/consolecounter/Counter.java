package com.example.consolecounter;

import org.springframework.beans.factory.annotation.Value;

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
