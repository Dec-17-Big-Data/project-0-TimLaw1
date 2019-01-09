package com.revature.exceptions;

public class InvalidOptionException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2526354196533180600L;

	public InvalidOptionException(String errorMessage) {
        super(errorMessage);
    }
}
