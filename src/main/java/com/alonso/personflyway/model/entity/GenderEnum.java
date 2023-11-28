package com.alonso.personflyway.model.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// This is to make ENUM into POJO in JSON format

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum GenderEnum {
	MALE, FEMALE;
}
