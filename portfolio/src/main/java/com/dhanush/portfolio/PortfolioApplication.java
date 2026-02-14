package com.dhanush.portfolio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@OpenAPIDefinition(
        info = @Info(
                title = "Photographer Portfolio API",
                version = "1.0",
                description = "API for managing photographer portfolio with Cloudinary Integration"
        )
)
public class PortfolioApplication {

    public static void main(String[] args) {
        SpringApplication.run(PortfolioApplication.class, args);
        System.out.println("■■ Portfolio Application Started Successfully!");
    }
}