package com.gelo.spirum;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
@SpringBootApplication
public class SpirumApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpirumApplication.class, args);
	}

	@Bean //this could be provided via auto-configuration
	MethodValidationPostProcessor methodValidationPostProcessor() {
		return new MethodValidationPostProcessor();
	}
}
