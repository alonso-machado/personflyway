package com.alonso.personflyway.model.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDTO implements Serializable {

	private Integer id;
	@NotEmpty
	@Size(min = 1, max = 50)
	private String fullName;
	@JsonFormat(pattern = "yyyy-MM-dd") // ISO8601 - US / International Standard
	private LocalDate birthdate;
	@NotEmpty
	private String gender;

}
