package com.alonso.personflyway.controller;

import com.alonso.personflyway.model.dtos.PersonDTO;
import com.alonso.personflyway.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping("/api/v1/person")
public class PersonController {

	private PersonService service;

	@GetMapping("/")
	@Operation(summary = "Return all the Persons in the System (Default is 20 per page)")
	public ResponseEntity<Page<PersonDTO>> getAll(@RequestParam(required = false, defaultValue = "0") @PositiveOrZero @Valid Integer page,
	                                              @RequestParam(required = false, defaultValue = "20") @Max(value = 20) @Min(value = 1) @Valid Integer size) {

		return ResponseEntity.ok(service.findAll(page, size));
	}

	@PostMapping("/")
	@Operation(summary = "Add a new Person in the System.")
	public ResponseEntity<PersonDTO> addPerson(@Parameter(description = "The Full Name of the Person ") @Valid @RequestParam @Length(min = 1, max = 50) String fullName,
	                                           @Parameter(description = "The Gender of the Person", required = true) @RequestParam @Valid String gender,
	                                           @Parameter(description = "The Birthday of the Person (yyyy-MM-dd)", required = true) @RequestParam LocalDate birthdate) {
		return ResponseEntity.ok(service.savePerson(fullName, gender, birthdate));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Get a Person with that ID.")
	public ResponseEntity<PersonDTO> getPerson(@NotNull @PathVariable Integer id) {
		return ResponseEntity.ok(service.findById(id));
	}

	@PatchMapping("/{id}")
	@Operation(summary = "Update Fields of that specific Person in the System.")
	public ResponseEntity<PersonDTO> updatePerson(@NotNull @PathVariable Integer id, @RequestBody Map<String, Object> updatingFields) {
		return ResponseEntity.ok(service.updatePersonParts(id, updatingFields));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Update that specific Person in the System.")
	public ResponseEntity<PersonDTO> updateWholePerson(@NotNull @PathVariable Integer id, @RequestBody PersonDTO person) {
		return ResponseEntity.ok(service.updatePerson(id, person));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Delete that Person from the System with that ID.")
	public ResponseEntity<String> deletePerson(@NotNull @PathVariable Integer id) {
		service.delete(id);
		return new ResponseEntity<>("Person deleted successfully.", HttpStatus.OK);
	}
}
