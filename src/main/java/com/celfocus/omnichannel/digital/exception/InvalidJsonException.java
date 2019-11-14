package com.celfocus.omnichannel.digital.exception;

public class InvalidJsonException extends RuntimeException {
	
	private static final long serialVersionUID = 1487433103722736573L;

	public InvalidJsonException() {
		super();
	}
	
	public InvalidJsonException(String message) {
		super(message);
	}
	
	public InvalidJsonException(String message, Throwable t) {
		super(message, t);
	}
	
	public InvalidJsonException(Throwable t) {
		super(t);
	}

}
