package com.example.twinklechat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class TwinklechatApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwinklechatApplication.class, args);
	}

}
