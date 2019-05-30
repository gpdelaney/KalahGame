package com.backbase.kalah.exceptions;

/**
 * Exception to be thrown if the game is not found
 * 
 * @author willdelaney
 *
 */
@SuppressWarnings("serial")
public class InvalidGameException extends Exception{
	public InvalidGameException(String errorMessage) {
		super(errorMessage);
	}

}
