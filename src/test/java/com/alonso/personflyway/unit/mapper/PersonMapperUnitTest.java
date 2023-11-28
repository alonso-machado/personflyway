package com.alonso.personflyway.unit.mapper;

import com.alonso.personflyway.mapper.PersonMapper;
import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.model.entity.Person;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PersonMapperUnitTest {


	@Test
	void whenConvertDTOToEntity_thenReturnEntity() {
		// Arrange
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Person expectedPerson = Person.builder().id(personTestId).fullName("Test Person Mapper").birthdate(testDate).gender(newGender).build();
		PersonDTO expectedPersonDTO = PersonDTO.builder().id(personTestId).fullName("Test Person Mapper").birthdate(testDate).build();

		//Act
		Person returned = PersonMapper.toEntity(expectedPersonDTO, newGender);

		//Assert
		assertAll("Grouped Assertions of Purchase",
				() -> assertEquals(returned.getId(), expectedPerson.getId()),
				() -> assertEquals(returned.getFullName(), expectedPerson.getFullName()),
				() -> assertEquals(returned.getBirthdate(), expectedPerson.getBirthdate()),
				() -> assertEquals(returned.getGender(), expectedPerson.getGender())
		);

	}

	@Test
	void whenConvertEntityToDTO_thenReturnDTO() {
		//Arrange
		String mapperEntitytoDTO = "MapperEntitytoDTO";
		Integer personTestId = 1;
		LocalDate testDate = LocalDate.now();
		Gender newGender = Gender.builder().id(personTestId).name("MALE").build();
		Person person = Person.builder().id(personTestId).fullName(mapperEntitytoDTO).birthdate(testDate).gender(newGender).build();
		PersonDTO expectedPersonDTO = PersonDTO.builder().id(personTestId).fullName(mapperEntitytoDTO).birthdate(testDate).gender(newGender.getName()).build();

		//Act
		PersonDTO returned = PersonMapper.toDto(person);

		//Assert
		assertAll("Grouped Assertions of PurchaseDTO",
				() -> assertEquals(returned.getId(), expectedPersonDTO.getId()),
				() -> assertEquals(returned.getFullName(), expectedPersonDTO.getFullName()),
				() -> assertEquals(returned.getBirthdate(), expectedPersonDTO.getBirthdate()),
				() -> assertEquals(returned.getGender(), expectedPersonDTO.getGender())
		);
	}
}
