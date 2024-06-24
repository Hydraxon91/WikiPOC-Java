package com.hydraxon91.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
		"com.hydraxon91.backend.services",
		"com.hydraxon91.backend.config",
		"com.hydraxon91.backend.repositories",
		"com.hydraxon91.backend.controllers",
		"com.hydraxon91.backend.security"
})public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}