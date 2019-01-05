package com.revature.exceptions;

public class IncorrectPasswordException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4278546974494424222L;
    public IncorrectPasswordException(String errorMessage) {
        super(errorMessage);
    }
}
