package com.property.dealwithme;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DealwithmeApplication {

	public static void main(String[] args) {
		SpringApplication.run(DealwithmeApplication.class, args);
	}

}
