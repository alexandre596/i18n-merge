package com.celfocus.omnichannel.digital.exception;

public class GitException extends Exception {
	
	private static final long serialVersionUID = 1920588273523663823L;

	public GitException() {
		super();
	}
	
	public GitException(String message) {
		super(message);
	}
	
	public GitException(String message, Throwable t) {
		super(message, t);
	}
	
	public GitException(Throwable t) {
		super(t);
	}

}
