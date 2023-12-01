package com.alonso.personflyway.service;

import com.alonso.personflyway.exceptions.GenderNotInDatabaseException;
import com.alonso.personflyway.exceptions.PersonNotFoundInDatabaseException;
import com.alonso.personflyway.mapper.PersonMapper;
import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.model.entity.Person;
import com.alonso.personflyway.repository.GenderRepository;
import com.alonso.personflyway.repository.PersonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@AllArgsConstructor
public class PersonServiceImpl implements PersonService {

	private PersonRepository personRepository;
	private GenderRepository genderRepository;


	@Override
	public Page<PersonDTO> findAll(Integer page, Integer size) {
		PageRequest pageable = PageRequest.of(page, size, Sort.Direction.ASC, "id");
		return personRepository.findAll(pageable).map(PersonMapper::toDto);
	}


	@Override
	public PersonDTO findById(Integer id) {
		Person person = personRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Person with this ID does not Exist in our System!"));
		return PersonMapper.toDto(person);
	}


	@Override
	public PersonDTO savePerson(String name, String genderString, LocalDate birthdate) {
		Gender gender = genderRepository.findByName(genderString.toUpperCase()).orElseThrow(GenderNotInDatabaseException::new);
		Person newPerson = Person.builder().fullName(name).gender(gender).birthdate(birthdate).build();
		personRepository.save(newPerson);
		return PersonMapper.toDto(newPerson);
	}


	@Override
	public PersonDTO updatePersonParts(Integer id, Map<String, Object> updatingFields) {
		Person previous = personRepository.findById(id).orElseThrow(PersonNotFoundInDatabaseException::new);
		List<String> fieldNames = Arrays.stream(Person.class.getDeclaredFields()).map(x -> x.getName()).toList();
		if (updatingFields.keySet().stream().anyMatch(fieldNames::contains)) {
			updatingFields.forEach((key, value) -> {
				Field f = ReflectionUtils.findField(Person.class, key);
				if (fieldNames.contains(key)) { //This seems overhead, but it protects again edge cases when PATCH enters with extra / misspelled fields
					if (!key.equals("birthdate")) {
						f.setAccessible(true); //To Handle Private Fields
						ReflectionUtils.setField(f, previous, value);
					} else {
						previous.setBirthdate(LocalDate.parse(value.toString()));
					}
				}
			});
			personRepository.save(previous);
		} else {
			throw new IllegalArgumentException("There is no Valid Field to be Updated! The Person will stay the same!");
		}
		return PersonMapper.toDto(previous);
	}

	@Override
	public PersonDTO updatePerson(Integer id, PersonDTO personDTO) {
		Person previous = personRepository.findById(id).orElseThrow(PersonNotFoundInDatabaseException::new);
		Gender gender = genderRepository.findByName(personDTO.getGender().toUpperCase()).orElseThrow(GenderNotInDatabaseException::new);
		Person putPerson = PersonMapper.toEntity(personDTO, gender);
		if(previous.getId().equals(personDTO.getId())) {
			personRepository.save(putPerson);
			return PersonMapper.toDto(putPerson);
		} else {
			throw new PersonNotFoundInDatabaseException();
		}
	}


	@Override
	public void delete(Integer id) {
		Person toDelete = personRepository.findById(id).orElseThrow(PersonNotFoundInDatabaseException::new);
		personRepository.delete(toDelete);
	}
}
