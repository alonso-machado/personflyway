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

	private PersonService purchaseService;

	@Mock
	private PersonRepository purchaseRepository;

	@Mock
	private GenderRepository genderRepository;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.initMocks(this);
		purchaseService = new PersonServiceImpl(purchaseRepository, genderRepository);
	}

	@Test
	void whenFindById_ShouldReturnPersonDTO() {
		// Arrange
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Person expectedPurchase = Person.builder().id(personTestId).fullName("Test Person Service").birthdate(testDate).gender(newGender).build();
		PersonDTO expectedPurchaseDTO = PersonDTO.builder().id(personTestId).fullName("Test Person Service").birthdate(testDate).build();

		when(purchaseRepository.findById(personTestId)).thenReturn(Optional.of(expectedPurchase));

		// Act
		PersonDTO result = purchaseService.findById(personTestId);

		// Assert
		assertNotNull(result);
		assertEquals(expectedPurchaseDTO.getFullName(), result.getFullName());
		assertEquals(expectedPurchaseDTO.getBirthdate(), result.getBirthdate());
		verify(purchaseRepository, times(1)).findById(personTestId);
	}

	@Test
	void whenFindById_ShouldThrowException_WhenPurchaseNotFound() {
		// Arrange
		Integer personTestId = 1;
		when(purchaseRepository.findById(personTestId)).thenReturn(Optional.empty());

		// Act and Assert
		assertThrows(IllegalArgumentException.class, () -> purchaseService.findById(personTestId));
		verify(purchaseRepository, times(1)).findById(personTestId);
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
		when(purchaseRepository.save(any(Person.class))).thenReturn(newPerson);
		when(genderRepository.findByName(newGender.getName())).thenReturn(Optional.of(newGender));

		// Act
		PersonDTO result = purchaseService.savePerson(newPerson.getFullName(), newGender.getName(), testDate);

		// Assert
		assertNotNull(result);
		assertEquals(expectedPersonDTO.getBirthdate(), result.getBirthdate());
		assertEquals(expectedPersonDTO.getFullName(), result.getFullName());
		assertEquals(expectedPersonDTO.getGender(), result.getGender());
		verify(purchaseRepository, times(1)).save(any(Person.class));
	}

	@Test
	void whenAddPerson_ShouldReturnInvalidGender(){
		// Arrange
		String name = "Test Person Service";
		LocalDate testDate = LocalDate.now();


		// ACT and Assert
		Assertions.assertThrows(IllegalArgumentException.class, () -> purchaseService.savePerson(name, "SUPERMAN", testDate));
	}

	@Test
	void whenUpdate_ShouldReturnUpdatedPersonDTO() throws NoSuchFieldException, IllegalAccessException {
		// Arrange
		String name = "Test Person Service";
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		LocalDate updatedTestDate = LocalDate.of(2022, 1, 1);
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Map<String, Object> updatingFields = new HashMap<>();
		updatingFields.put("fullName", "Updated Person");
		updatingFields.put("birthdate", updatedTestDate);

		Person existingPersonMocked = Person.builder().id(personTestId).fullName(name).birthdate(testDate).gender(newGender).build();

		Person updatedPerson = Person.builder().id(personTestId).fullName("Updated Person").birthdate(updatedTestDate).gender(newGender).build();

		PersonDTO expectedUpdatedPersonDTO = PersonDTO.builder().id(personTestId).fullName("Updated Person").gender(newGender.getName()).birthdate(updatedTestDate).build();

		// Mock the repository method
		when(purchaseRepository.findById(personTestId)).thenReturn(Optional.of(existingPersonMocked));
		when(purchaseRepository.save(any(Person.class))).thenReturn(updatedPerson);

		// Act
		PersonDTO result = purchaseService.updatePersonParts(personTestId, updatingFields);

		// Assert
		assertNotNull(result);
		assertEquals(expectedUpdatedPersonDTO.getFullName(), result.getFullName());
		assertEquals(expectedUpdatedPersonDTO.getBirthdate(), result.getBirthdate());
		verify(purchaseRepository, times(1)).findById(personTestId);
		verify(purchaseRepository, times(1)).save(any(Person.class));
	}

	@Test
	void whenDelete_ShouldDeletePurchase() {
		// Arrange
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Person optionalPerson = Person.builder().id(personTestId).fullName("Test Person Service").birthdate(testDate).gender(newGender).build();

		when(purchaseRepository.findById(personTestId)).thenReturn(Optional.of(optionalPerson));

		// Act
		purchaseService.delete(personTestId);

		// Assert
		verify(purchaseRepository, times(1)).findById(personTestId);
		verify(purchaseRepository, times(1)).delete(optionalPerson);
	}

}