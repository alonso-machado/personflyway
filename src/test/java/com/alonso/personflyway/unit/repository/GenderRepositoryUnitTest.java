package com.alonso.personflyway.unit.repository;

import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.repository.GenderRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@RunWith(SpringRunner.class)
public class GenderRepositoryUnitTest {

	@Autowired
	private GenderRepository genderRepository;


	@Before
	public void setup() {
		genderRepository.deleteAll();
	}

	@Test
	public void whenSave_thenReturnGender() {
		// Arrange
		Gender genderForTest = Gender.builder().id(1).name("FEMALE").build();


		//Act
		Gender found = genderRepository.save(genderForTest);

		//Assert
		assertThat(found.getId()).isEqualTo(genderForTest.getId());
		assertThat(found.getName()).isEqualTo(genderForTest.getName());
	}

	@Test
	public void whenFindById_thenReturnGender() {
		//Arrange
		Gender newGender = Gender.builder().id(1).name("SUPERFEMALE").build();

		//Save in the DB
		genderRepository.save(newGender);

		//Act
		Gender found = genderRepository.findById(newGender.getId()).get();

		//Assert
		assertThat(found.getName()).isEqualTo(newGender.getName());
	}

	@Test(expected = ConstraintViolationException.class)
	public void whenFindByNameCaseNonexistent_thenReturnException() {
		String nonexistent = "GenderUnitTestNameNonexistent56789";

		genderRepository.findByName(nonexistent);
	}

}
