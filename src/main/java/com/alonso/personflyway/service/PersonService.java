package com.alonso.personflyway.service;

import com.alonso.personflyway.exceptions.GenderNotInDatabaseException;
import com.alonso.personflyway.model.dtos.PersonDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.Map;

public interface PersonService {
	Page<PersonDTO> findAll(Integer page, Integer size);

	PersonDTO findById(Integer id);

	public PersonDTO savePerson(String name, String gender, LocalDate birthdate);

	PersonDTO updatePersonParts(Integer id, Map<String, Object> updatingFields);

	PersonDTO updatePerson(Integer id, PersonDTO personDTO);

	void delete(Integer id);
}
