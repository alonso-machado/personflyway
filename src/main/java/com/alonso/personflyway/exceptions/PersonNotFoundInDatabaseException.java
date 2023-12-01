package com.alonso.personflyway.exceptions;

public class PersonNotFoundInDatabaseException extends IllegalArgumentException {

	public PersonNotFoundInDatabaseException() {
		super("Person with this ID does not Exist in our System!");
	}
}
