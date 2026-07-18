package com.gamma.backend.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.gamma.backend.repository")
@ComponentScan(basePackages = "com.gamma.backend")
@EntityScan(basePackages = "com.gamma.backend.model")

public class GammaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(GammaBackendApplication.class, args);
	}

}
