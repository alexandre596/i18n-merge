package com.celfocus.omnichannel.digital.exception;

public class CouldNotLocateCorrectFileException extends InvalidFileException {
	
	private static final long serialVersionUID = 4383658083362343624L;

	public CouldNotLocateCorrectFileException() {
		super();
	}
	
	public CouldNotLocateCorrectFileException(String message) {
		super(message);
	}
	
	public CouldNotLocateCorrectFileException(String message, Throwable t) {
		super(message, t);
	}
	
	public CouldNotLocateCorrectFileException(Throwable t) {
		super(t);
	}

}
