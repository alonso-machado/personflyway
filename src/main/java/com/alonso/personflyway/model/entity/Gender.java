package com.alonso.personflyway.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "TB_GENDER")
public class Gender {

	@Id
	@GeneratedValue
	@Column(name="ID_GENDER")
	@JsonIgnore
	private Integer id;
	private String name;
}
