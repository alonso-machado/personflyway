package com.alonso.personflyway.exceptions;

public class GenderNotInDatabaseException extends IllegalArgumentException {

	public GenderNotInDatabaseException() {
		super("This Gender is not Registered in our System! / Contact Admin to Register it");
	}
}
