package com.alonso.personflyway.controller;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@AllArgsConstructor
@Validated
@RequestMapping("/pages")
public class ThymeleafController {

	private PersonService service;

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

	//This is a workaround to delete using POST (Only one in HTML forms)
	@PostMapping("/delete/confirm/{id}")
	public String deletePersonUsingId(@NotNull @PathVariable Integer id) {
		service.delete(id);
		return "redirect:/pages/?page=0";
	}
}
