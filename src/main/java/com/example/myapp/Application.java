package com.example.myapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // Spring Boot 애플리케이션 시작 어노테이션
public class Application {
    public static void main(String[] args) {
        System.out.println(">>> App Started <<<");
        SpringApplication.run(Application.class, args);
    }
}

