package com.alonso.personflyway.integration.controller;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.service.PersonService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClient;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonControllerIntegrationTest {

	@LocalServerPort
	private int port;
	private String uriBase;
	RestClient restClient = RestClient.create();

	@Autowired
	private PersonService personService;

	@BeforeEach
	public void setup() {
		uriBase = "http://localhost:" + port;
	}

	@Test
	void whenGetPerson_returnPersonDTO() throws Exception {
		String personName = "Controller Integration Full";
		PersonDTO savedPerson = personService.savePerson(personName, "MALE", LocalDate.now());

		RestClient.ResponseSpec response = restClient.get().uri(uriBase+"/api/v1/person/{id}", savedPerson.getId()).retrieve();

		Assertions.assertEquals(response.body(PersonDTO.class).getFullName(), personName);
	}

	@Test
	void whenPostPerson_returnPersonDTO() throws Exception {
		String personName = "Controller Integration Full";
		PersonDTO savedPerson = personService.savePerson(personName, "MALE", LocalDate.now());
		String encodedParamsURI = encodePersonDTOWithParams(savedPerson, "/api/v1/person/");

		RestClient.ResponseSpec response = restClient.post().uri(encodedParamsURI)
				.contentType(APPLICATION_JSON).body(savedPerson).retrieve();

		Assertions.assertEquals(response.body(PersonDTO.class).getFullName(), personName);
	}

	private String encodeValue(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	}

	private String encodePersonDTOWithParams(PersonDTO savedPerson, String apiEndpoint){
		Map<String, String> requestParams = new HashMap<>();
		requestParams.put("fullName", savedPerson.getFullName());
		requestParams.put("gender", savedPerson.getGender());
		requestParams.put("birthdate", savedPerson.getBirthdate().toString());
		String baseURI = uriBase.concat(apiEndpoint);
		return requestParams.keySet().stream()
				.map(key -> {
					try {
						return key + "=" + encodeValue(requestParams.get(key));
					} catch (UnsupportedEncodingException e) {
						throw new RuntimeException(e);
					}
				})
				.collect(joining("&", baseURI+"?", ""));
	}

	@Test
	void whenPostPersonBigName_returnException() throws Exception {
		String personName = "ServiceTestDescription1234567890123456789012345678901234567890123456789012345678901234567890";;
		PersonDTO savedPerson = personService.savePerson(personName, "MALE", LocalDate.now());
		String encodedParamsURI = encodePersonDTOWithParams(savedPerson, "/api/v1/person/");


		/*
		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			restClient.post().uri(encodedParamsURI)
					.contentType(APPLICATION_JSON).body(savedPerson).retrieve();
		});
		*/
	}

}