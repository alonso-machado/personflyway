package com.alonso.personflyway.unit.controller;

import com.alonso.personflyway.controller.PersonController;
import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PurchaseControllerUnitTest {

	private PersonController personController;
	private PersonService personService;

	@BeforeEach
	public void setup() {
		personService = mock(PersonService.class);
		personController = new PersonController(personService);
	}

	@Test
	void whenGetAll_thenReturnEmptyPage() {
		// Arrange
		Pageable pageable = Pageable.ofSize(20);
		Page<PersonDTO> expectedPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
		when(personService.findAll(1, 1)).thenReturn(expectedPage);

		// Act
		ResponseEntity<Page<PersonDTO>> response = personController.getAll(1, 1);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPage, response.getBody());
	}

	@Test
	void whenGetById_thenReturnPerson() {
		// Arrange
		Integer personTestId = 1;
		String nameForTest = "Controller Female Name Miss Test";
		Gender newGender = Gender.builder().id(personTestId).name("FEMALE").build();
		LocalDate testDate = LocalDate.now();

		PersonDTO expectedPersonDTO = PersonDTO.builder().id(personTestId).fullName(nameForTest).gender(newGender.getName()).birthdate(testDate).build();
		when(personService.findById(personTestId)).thenReturn(expectedPersonDTO);

		// Act
		ResponseEntity<PersonDTO> response = personController.getPerson(personTestId);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPersonDTO, response.getBody());
	}

	@Test
	void whenAddPurchase_thenReturnPerson() {
		// Arrange
		Integer personTestId = 1;
		String nameForTest = "Controller Female Name Miss Test";
		Gender newGender = Gender.builder().id(personTestId).name("FEMALE").build();
		LocalDate testDate = LocalDate.now();

		PersonDTO expectedPersonDTO = PersonDTO.builder().id(personTestId).fullName(nameForTest).gender(newGender.getName()).birthdate(testDate).build();

		when(personService.savePerson(nameForTest, newGender.getName(), testDate)).thenReturn(expectedPersonDTO);

		// Act
		ResponseEntity<PersonDTO> response = personController.addPerson(nameForTest, newGender.getName(), testDate);

		// Assert
		assertEquals(HttpStatus.OK, response.getStatusCode());
		assertEquals(expectedPersonDTO, response.getBody());
		assertEquals(response.getBody().getBirthdate(), testDate);
		assertEquals(response.getBody().getFullName(), nameForTest);
	}

}
