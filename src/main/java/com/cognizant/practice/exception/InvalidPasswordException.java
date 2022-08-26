package com.cognizant.practice.exception;

public class InvalidPasswordException extends Exception {

	private static final long serialVersionUID = -4676695167058983857L;
	
	public InvalidPasswordException(String message) {
		super(message);
	}

}
