package com.invoices.invoices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("com.invoices.invoices.models")
public class InvoicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(InvoicesApplication.class, args);
	}

}
