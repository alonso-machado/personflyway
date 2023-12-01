package com.alonso.personflyway.unit.repository;

import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.model.entity.Person;
import com.alonso.personflyway.repository.PersonRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryUnitTest {
/*
	@Autowired
	private PersonRepository personRepository;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(personRepository);
	}

	@Test
	void whenSave_thenReturnPerson() {
		// Arrange
		Integer personTestId = 1;
		String nameForTest = "Test Person Repository";
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("FEMALE").build();
		Person person = Person.builder().id(personTestId).fullName(nameForTest).birthdate(testDate).gender(newGender).build();

		//Act
		Person found = personRepository.save(person);

		//Assert
		assertThat(found.getId()).isEqualTo(person.getId());
		assertThat(found.getBirthdate()).isEqualTo(testDate);
		assertThat(found.getFullName()).isEqualTo(nameForTest);
	}


	@Test
		// More than 50 Characters on Full Name should be Invalid
	void whenSaveDescriptionLongInvalid_thenConstraintViolationException() throws ConstraintViolationException {
		String nameForTest = "RepositoryTestDescription1234567890123456789012345678901234567890123456789012345678901234567890";
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(1).name("FEMALE").build();
		Person person = Person.builder().id(1).fullName(nameForTest).birthdate(testDate).gender(newGender).build();


		Assertions.assertThrows(ConstraintViolationException.class,
				() -> personRepository.save(person));
	}

	@Test
	void whenFindById_thenReturnPerson() {
		//Arrange
		String nameForTest = "Repository Test Name";
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(1).name("FEMALE").build();
		Person person = Person.builder().id(1).fullName(nameForTest).birthdate(testDate).gender(newGender).build();

		//Save in the DB
		personRepository.save(person);

		//Act
		Person found = personRepository.findById(person.getId()).get();

		//Assert
		assertThat(found.getId()).isEqualTo(person.getId());
		assertThat(found.getFullName()).isEqualTo(person.getFullName());
		assertThat(found.getBirthdate()).isEqualTo(person.getBirthdate());
	}
*/
}
