package com.alonso.personflyway.unit.repository;

import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.repository.GenderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class GenderRepositoryUnitTest {

	@Autowired
	private GenderRepository genderRepository;


	@Test
	void contextLoads() {
		Assertions.assertNotNull(genderRepository);
	}

	@Test
	void whenSave_thenReturnGender() {
		// Arrange
		Gender genderForTest = Gender.builder().id(1).name("FEMALE").build();


		//Act
		Gender found = genderRepository.save(genderForTest);

		//Assert
		assertThat(found.getId()).isEqualTo(genderForTest.getId());
		assertThat(found.getName()).isEqualTo(genderForTest.getName());
	}

	@Test
	void whenFindById_thenReturnGender() {
		//Arrange
		Gender newGender = Gender.builder().id(1).name("SUPERFEMALE").build();

		//Save in the DB
		genderRepository.save(newGender);

		//Act
		Gender found = genderRepository.findById(newGender.getId()).get();

		//Assert
		assertThat(found.getName()).isEqualTo(newGender.getName());
	}

	@Test
	void whenFindByNameCaseNonexistent_thenReturnException() {
		String nonexistent = "GenderUnitTestNameNonexistent56789";

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			genderRepository.findByName(nonexistent);
		});
	}

}
