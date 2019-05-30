package com.backbase.kalah.exceptions;

/**
 * Exception to be thrown when the Cup selected is invalid (out of range or no stones in it)
 * @author willdelaney
 *
 */
@SuppressWarnings("serial")
public class InvalidCupException extends Exception{
	
	public InvalidCupException(String errorMessage) {
		super(errorMessage);
	}

}
