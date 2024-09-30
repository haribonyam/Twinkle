package com.example.twinkle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TwinkleApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwinkleApplication.class, args);
	}

}
