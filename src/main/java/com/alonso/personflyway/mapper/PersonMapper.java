package com.alonso.personflyway.mapper;


import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.model.entity.Person;

// Mapper to Convert to DTO or to Entity
// Doing this by hand usually get more Performance / Memory usage then using ModelMapper or MapStruct
public class PersonMapper {

	public static Person toEntity(PersonDTO pDto, Gender genderFromRepo) {

		Person p = new Person();
		p.setId(pDto.getId());
		p.setFullName(pDto.getFullName());
		p.setBirthdate(pDto.getBirthdate());
		p.setGender(genderFromRepo); //This is to ensure that the Gender is set accordingly to Repo and avoid logic inside the Mapper

		return p;
	}

	public static PersonDTO toDto(Person person) {
		PersonDTO purchaseDTO = PersonDTO.builder()
				.id(person.getId())
				.birthdate(person.getBirthdate())
				.fullName(person.getFullName())
				.gender(person.getGender().getName())
				.build();
		return purchaseDTO;
	}

}
