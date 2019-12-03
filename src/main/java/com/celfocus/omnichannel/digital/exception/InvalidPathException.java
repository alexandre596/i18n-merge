package com.celfocus.omnichannel.digital.exception;

public class InvalidPathException extends Exception {
	
	private static final long serialVersionUID = 77557562520008494L;

	public InvalidPathException() {
		super();
	}
	
	public InvalidPathException(String message) {
		super(message);
	}
	
	public InvalidPathException(String message, Throwable t) {
		super(message, t);
	}
	
	public InvalidPathException(Throwable t) {
		super(t);
	}

}
