package com.alonso.personflyway.unit.service;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.model.entity.Person;
import com.alonso.personflyway.repository.GenderRepository;
import com.alonso.personflyway.repository.PersonRepository;
import com.alonso.personflyway.service.PersonService;
import com.alonso.personflyway.service.PersonServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PersonServiceImplTest {

	private PersonService personService;

	@Mock
	private PersonRepository personRepository;

	@Mock
	private GenderRepository genderRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		personService = new PersonServiceImpl(personRepository, genderRepository);
	}

	@Test
	void whenFindById_ShouldReturnPersonDTO() {
		// Arrange
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Person expectedPurchase = Person.builder().id(personTestId).fullName("Test Person Service").birthdate(testDate).gender(newGender).build();
		PersonDTO expectedPurchaseDTO = PersonDTO.builder().id(personTestId).fullName("Test Person Service").birthdate(testDate).build();

		when(personRepository.findById(personTestId)).thenReturn(Optional.of(expectedPurchase));

		// Act
		PersonDTO result = personService.findById(personTestId);

		// Assert
		assertNotNull(result);
		assertEquals(expectedPurchaseDTO.getFullName(), result.getFullName());
		assertEquals(expectedPurchaseDTO.getBirthdate(), result.getBirthdate());
		verify(personRepository, times(1)).findById(personTestId);
	}

	@Test
	void whenFindById_ShouldThrowException_WhenPurchaseNotFound() {
		// Arrange
		Integer personTestId = 1;
		when(personRepository.findById(personTestId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> personService.findById(personTestId));
		verify(personRepository, times(1)).findById(personTestId);
	}

	@Test
	void whenAddPerson_ShouldReturnPurchaseDTO(){
		// Arrange
		String name = "Test Person Service";
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Person newPerson = Person.builder().id(personTestId).fullName(name).birthdate(testDate).gender(newGender).build();
		PersonDTO expectedPersonDTO = PersonDTO.builder().id(personTestId).fullName(name).gender(newGender.getName()).birthdate(testDate).build();

		// Mock the repository method
		when(personRepository.save(any(Person.class))).thenReturn(newPerson);
		when(genderRepository.findByName(newGender.getName())).thenReturn(Optional.of(newGender));

		// Act
		PersonDTO result = personService.savePerson(newPerson.getFullName(), newGender.getName(), testDate);

		// Assert
		assertNotNull(result);
		assertEquals(expectedPersonDTO.getBirthdate(), result.getBirthdate());
		assertEquals(expectedPersonDTO.getFullName(), result.getFullName());
		assertEquals(expectedPersonDTO.getGender(), result.getGender());
		verify(personRepository, times(1)).save(any(Person.class));
	}

	@Test
	void whenAddPerson_ShouldReturnInvalidGender(){
		// Arrange
		String name = "Test Person Service";
		LocalDate testDate = LocalDate.now();


		// ACT and Assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> personService.savePerson(name, "SUPERMAN", testDate));
	}

	@Test
	void whenUpdate_ShouldReturnUpdatedPersonDTO() throws NoSuchFieldException, IllegalAccessException {
		// Arrange
		String name = "Test Person Service";
		String updatedName = "Updated Test Person";
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		LocalDate updatedTestDate = LocalDate.of(2022, 1, 1);
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Map<String, Object> updatingFields = new HashMap<>();
		updatingFields.put("fullName", updatedName);
		updatingFields.put("birthdate", updatedTestDate.toString());

		Person existingPersonMocked = Person.builder().id(personTestId).fullName(name).birthdate(testDate).gender(newGender).build();

		Person updatedPerson = Person.builder().id(personTestId).fullName(updatedName).birthdate(updatedTestDate).gender(newGender).build();

		PersonDTO expectedUpdatedPersonDTO = PersonDTO.builder().id(personTestId).fullName(updatedName).gender(newGender.getName()).birthdate(updatedTestDate).build();

		// Mock the repository method
		when(personRepository.findById(personTestId)).thenReturn(Optional.of(existingPersonMocked));
		when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

		// Act
		PersonDTO result = personService.updatePersonParts(personTestId, updatingFields);

		// Assert
		assertNotNull(result);
		assertEquals(expectedUpdatedPersonDTO.getFullName(), result.getFullName());
		assertEquals(expectedUpdatedPersonDTO.getBirthdate(), result.getBirthdate());
		verify(personRepository, times(1)).findById(personTestId);
		verify(personRepository, times(1)).save(any(Person.class));
	}

	@Test
	void whenDelete_ShouldDeletePurchase() {
		// Arrange
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Person optionalPerson = Person.builder().id(personTestId).fullName("Test Person Service").birthdate(testDate).gender(newGender).build();

		when(personRepository.findById(personTestId)).thenReturn(Optional.of(optionalPerson));

		// Act
		personService.delete(personTestId);

		// Assert
		verify(personRepository, times(1)).findById(personTestId);
		verify(personRepository, times(1)).delete(optionalPerson);
	}

}