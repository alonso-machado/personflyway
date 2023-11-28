package com.alonso.personflyway.integration.service;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.model.entity.Person;
import com.alonso.personflyway.repository.GenderRepository;
import com.alonso.personflyway.repository.PersonRepository;
import com.alonso.personflyway.service.PersonService;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class PersonServiceIntegrationTest {

	String GENDER_NAME = "SUPERMANTEST";

	@Autowired
	private PersonService purchaseService;

	@Autowired
	private PersonRepository purchaseRepository;

	@Autowired
	private GenderRepository genderRepository;

	@BeforeAll
	public void genderSetup() {
		//SETUP Gender for all TESTS
		Gender newGender = Gender.builder().id(1).name(GENDER_NAME).build();
		genderRepository.save(newGender);
	}

	@BeforeEach
	public void setup() {
		purchaseRepository.deleteAll();
	}

	@Test
	@Order(1)
	void whenSave_thenReturnPurchase() {
		// Arrange
		String name = "Test Person Service";
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		PersonDTO expectedPersonDTO = PersonDTO.builder().id(personTestId).fullName(name).gender(GENDER_NAME).birthdate(testDate).build();

		//Act
		PersonDTO found = purchaseService.savePerson(name, GENDER_NAME, testDate);

		//Assert
		assertThat(found.getId()).isEqualTo(expectedPersonDTO.getId());
		assertThat(found.getBirthdate()).isEqualTo(expectedPersonDTO.getBirthdate());
		assertThat(found.getFullName()).isEqualTo(expectedPersonDTO.getFullName());
	}

	@Test // More than 50 Characters on Description should be Invalid
	@Order(2)
	void whenSaveDescriptionLongInvalid_thenReturnConstraintViolationException() {
		String descriptionTest = "ServiceTestDescription1234567890123456789012345678901234567890123456789012345678901234567890";
		LocalDate nowDate = LocalDate.now();

		Assertions.assertThrows(ConstraintViolationException.class, () -> {
			purchaseService.savePerson(descriptionTest, GENDER_NAME, nowDate);
		});

	}

	@Test
	@Order(3)
	void whenFindById_thenReturnPurchaseDTO() {
		//Arrange
		String name = "Test Person Service";
		LocalDate testDate = LocalDate.now();


		//Save in the DB
		PersonDTO saved = purchaseService.savePerson(name, GENDER_NAME, testDate);

		//Act
		PersonDTO found = purchaseService.findById(saved.getId());

		//Assert
		assertThat(found.getFullName()).isEqualTo(saved.getFullName());
		assertThat(found.getGender()).isEqualTo(saved.getGender());
		assertThat(found.getBirthdate()).isEqualTo(saved.getBirthdate());
	}

}
