package com.example.challenge_mottu_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "API da Mottu", description = "Realização de projeto academico em parceria com a Mottu", version = "v1"))
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}
