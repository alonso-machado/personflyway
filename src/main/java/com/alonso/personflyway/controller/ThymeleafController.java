package com.alonso.personflyway.controller;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@Validated
@RequestMapping("/pages")
public class ThymeleafController {

	private PersonService service;

	@GetMapping("/")
	public String index(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero @Valid Integer page,
	                         @RequestParam(required = false, defaultValue = "20") @Max(value = 20) @Min(value = 1) @Valid Integer size,
	                         Model model) {
		model.addAttribute("persons", service.findAll(page, size));

		return "index.html";
	}

	@GetMapping("/save/")
	@Operation(summary = "Return Thymeleaf Page to Create that Person in the System.")
	public String savePersonPage(Model model) {
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
}
