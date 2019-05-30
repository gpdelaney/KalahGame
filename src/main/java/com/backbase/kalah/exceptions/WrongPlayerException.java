package com.backbase.kalah.exceptions;

/**
 * Custom Exceptions to be thrown when the cup selection is invalid for that
 * player.
 * 
 * @author willdelaney
 *
 */
@SuppressWarnings("serial")
public class WrongPlayerException extends Exception {

	public WrongPlayerException(String errorMessage) {
		super(errorMessage);
	}

}
