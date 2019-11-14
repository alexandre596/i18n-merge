package com.celfocus.omnichannel.digital.exception;

public class InvalidFileException extends Exception {
	
	private static final long serialVersionUID = 4383658083362343624L;

	public InvalidFileException() {
		super();
	}
	
	public InvalidFileException(String message) {
		super(message);
	}
	
	public InvalidFileException(String message, Throwable t) {
		super(message, t);
	}
	
	public InvalidFileException(Throwable t) {
		super(t);
	}

}
