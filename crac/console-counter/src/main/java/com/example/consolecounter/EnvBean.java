package com.example.consolecounter;

import org.springframework.beans.factory.annotation.Value;

public class EnvBean {
    @Value("${deploy.stage}")
    private String deployStage;

    public String getDeployStage() {
        return deployStage;
    }
}
