package com.alonso.personflyway;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableJpaRepositories(basePackages="com.alonso.personflyway.repository")
@EnableTransactionManagement
@EnableWebMvc
@OpenAPIDefinition(info = @Info(title = "Person with Flyway API", version = "1.0", description = "Documentation Person API v1.0"))
public class PersonflywayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonflywayApplication.class, args);
	}

}
