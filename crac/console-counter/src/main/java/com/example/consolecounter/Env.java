package com.example.consolecounter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Env {
    @Value("${deploy.stage}")
    private String deployStage;

    public String getDeployStage() {
        return deployStage;
    }
}
