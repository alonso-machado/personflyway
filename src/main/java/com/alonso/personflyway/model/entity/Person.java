package com.alonso.personflyway.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="TB_PERSON")
public class Person implements Serializable {
	@Id
	@GeneratedValue
	@Column(name="ID_PERSON")
	private Integer id;
	@Column(name="FULL_NAME", length = 50)
	@Size(min = 1, max = 50)
	private String fullName;
	@Column(name="BIRTHDATE")
	private LocalDate birthdate;
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name="gender_id",referencedColumnName = "ID_GENDER")
	private Gender gender;
}
