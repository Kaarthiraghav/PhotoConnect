package com.example.PhotoConnect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class PhotoConnectApplication {

	public static void main(String[] args) {
		SpringApplication.run(PhotoConnectApplication.class, args);
	}

}
