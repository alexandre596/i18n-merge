package com.celfocus.omnichannel.digital.utils;

public final class StringUtils {
	
	private StringUtils() {
		super();
	}
	
	public static final String replaceCharacters(String string) {
		return string.replaceAll("\" : \"", "\": \"")
				.replaceAll("€", "\\\\u20ac")
				.replaceAll("’", "\\\\u2019");
	}

}
