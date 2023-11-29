package com.alonso.personflyway.integration.controller;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestClient;

import javax.swing.text.html.HTMLDocument;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlywayControllerIntegrationTest {

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
	void whenGetPage_returnPage() throws Exception {
		String personName = "Flyway Controller Integration";
		PersonDTO savedPerson = personService.savePerson(personName, "MALE", LocalDate.now());

		RestClient.ResponseSpec response = restClient.get().uri(uriBase + "/pages/delete/{id}", savedPerson.getId()).retrieve();
		Assertions.assertEquals(HttpStatus.OK,response.toBodilessEntity().getStatusCode());
		//Assertions.assertEquals(response.body(HTMLDocument.class).getElement("input[name='fullName']"), personName); // Problably if we could decode the HTML
	}

	@Test
	void whenPostPageForUpdate_returnPage() throws Exception {
		String personName = "Flyway Controller Integration";
		String updatedName = "777117777";
		LocalDate birthday = LocalDate.parse("1991-11-12");
		PersonDTO savedPerson = personService.savePerson(personName, "FEMALE", LocalDate.now());
		PersonDTO updatedPerson = PersonDTO.builder().id(savedPerson.getId()).fullName(updatedName).birthdate(birthday).gender(savedPerson.getGender()).build();
		String encodedParamsURI = encodePersonDTOWithParams(updatedPerson, "/pages/edit/put/");

		RestClient.ResponseSpec response = restClient.post().uri(encodedParamsURI).retrieve();

		Assertions.assertEquals(HttpStatus.FOUND, response.toBodilessEntity().getStatusCode()); // This is the Code for a successful update on Thymeleaf
		PersonDTO afterResponse = personService.findById(savedPerson.getId());
		Assertions.assertEquals(updatedName, afterResponse.getFullName());
		Assertions.assertEquals(birthday, afterResponse.getBirthdate());
	}

	private String encodeValue(String value) throws UnsupportedEncodingException {
		return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
	}

	private String encodePersonDTOWithParams(PersonDTO savedPerson, String apiEndpoint){
		Map<String, String> requestParams = new HashMap<>();
		requestParams.put("fullName", savedPerson.getFullName());
		requestParams.put("gender", savedPerson.getGender());
		requestParams.put("birthdate", savedPerson.getBirthdate().toString());
		String baseURI = uriBase.concat(apiEndpoint).concat(savedPerson.getId().toString());
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

}