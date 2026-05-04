package com.banking.java_banking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@OpenAPIDefinition(
	info  = @Info(
		title = "Java Banking API",
		version = "1.0",
		description = "API for managing banking operations",
		contact = @Contact(
			name = "Java Banking Team",
			email = "support@javabanking.com"
		)
	)
)
public class JavaBankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(JavaBankingApplication.class, args);
	}

}
