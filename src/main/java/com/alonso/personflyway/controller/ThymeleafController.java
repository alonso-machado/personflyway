package com.alonso.personflyway.controller;

import com.alonso.personflyway.exceptions.GenderNotInDatabaseException;
import com.alonso.personflyway.mapper.PersonMapper;
import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.model.entity.Gender;
import com.alonso.personflyway.model.entity.Person;
import com.alonso.personflyway.repository.GenderRepository;
import com.alonso.personflyway.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Controller
@AllArgsConstructor
@Validated
@RequestMapping("/pages")
public class ThymeleafController {

	ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	Validator validator = factory.getValidator();

	private PersonService service;
	private GenderRepository genderRepository;

	@GetMapping("/")
	public String indexPage(@RequestParam(defaultValue = "0") @Valid @PositiveOrZero Integer page, @NotNull Model model) {
		List<PersonDTO> serviceResponse = service.findAll(page, 20).getContent();
		model.addAttribute("personsList", serviceResponse);
		model.addAttribute("previousPage", page - 1 < 0 ? 0 : page - 1);
		model.addAttribute("nextPage", page + 1);
		return "index.html";
	}

	@GetMapping("/save/")
	@Operation(summary = "Return Thymeleaf Page to Create that Person in the System.")
	public String savePersonPage(@NotNull Model model) {
		model.addAttribute("person", new PersonDTO());
		return "saveperson.html";
	}

	@GetMapping("/edit/{id}")
	@Operation(summary = "Return Thymeleaf Page to Update that specific Person in the System.")
	public String updateWholePersonPage(@NotNull @PathVariable Integer id, Model model) {
		PersonDTO p = service.findById(id);
		model.addAttribute("person", service.updatePerson(id, p));
		return "showperson.html";
	}

	@GetMapping("/delete/{id}")
	@Operation(summary = "Return Thymeleaf Page to delete person.")
	public String deletePersonPage(@NotNull @PathVariable Integer id, Model model) {
		PersonDTO p = service.findById(id);
		model.addAttribute("person", service.updatePerson(id, p));
		return "deleteperson.html";
	}

	//This is to update using POST (Only one in HTML forms) and Thymeleaf Objects
	@PostMapping("/edit/put/{id}")
	public String savePersonUsingId(@NotNull @PathVariable Integer id, @ModelAttribute PersonDTO person) {

		service.updatePerson(id, person);
		return "redirect:/pages/?page=0";
	}

	@PostMapping("/edit/validation/{id}")
	public String savePersonWithValidationUsingId(@NotNull @PathVariable Integer id, @Valid PersonDTO person, BindingResult result, Model model) {
		validatePerson(person).stream().forEach(result::addError); //
		if (result.hasErrors()) {
			model.addAttribute("person", person); //Add person back to model to return to user
			model.addAttribute("errorsEntity", result); //Add Errors of Entity back to model to return to user
			return "showperson";
		}
		service.updatePerson(id, person);
		return "redirect:/pages/?page=0";
	}

	//This is a workaround to delete using POST (Only one in HTML forms)
	@PostMapping("/delete/confirm/{id}")
	public String deletePersonUsingId(@NotNull @PathVariable Integer id) {
		service.delete(id);
		return "redirect:/pages/?page=0";
	}

	public List<ObjectError> validatePerson(PersonDTO pDTO) {
		List<ObjectError> objectErrorList = new ArrayList<>();
		Gender foundGender = genderRepository.findByName(pDTO.getGender()).orElseThrow(GenderNotInDatabaseException::new);
		Set<ConstraintViolation<Person>> violations = validator.validate(PersonMapper.toEntity(pDTO, foundGender));
		violations.stream().forEach(x -> objectErrorList.add(new ObjectError(x.getConstraintDescriptor().toString(), x.getMessage())));
		return objectErrorList;
	}
}
