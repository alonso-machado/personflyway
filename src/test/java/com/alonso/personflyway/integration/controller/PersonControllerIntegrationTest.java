package com.alonso.personflyway.integration.controller;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;

@SpringBootTest
class PersonControllerIntegrationTest {

	@Autowired
	private RestTemplate restTemplate;

	//private RestClient restClient;

	@Autowired
	PersonService personService;

	@Test
	void whenGetPerson_returnPersonDTO() throws Exception {
		String personName = "Controller Integration Full";
		PersonDTO savedPerson = personService.savePerson(personName, "MALE", LocalDate.now());

		ResponseEntity<PersonDTO> response = restTemplate.exchange(
				"/api/v1/person/{id}",
				org.springframework.http.HttpMethod.GET,
				null,
				PersonDTO.class,
				savedPerson.getId());

		Assertions.assertEquals(response.getBody().getFullName(), personName);
	}
}