package com.msg;

import jakarta.annotation.PreDestroy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = Main.class)
public class Main {

    public static void main(final String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("percy: app started");
    }

    @SuppressWarnings("unused")
    @PreDestroy
    public void onExit() {
        System.out.println("percy: app terminated");
    }
}
