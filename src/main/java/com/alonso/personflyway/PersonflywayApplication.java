package com.alonso.personflyway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableJpaRepositories(basePackages="com.alonso.personflyway.repository")
@EnableTransactionManagement
public class PersonflywayApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonflywayApplication.class, args);
	}

}
