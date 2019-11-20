package com.celfocus.omnichannel.digital.helpers;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

public final class InternationalizationHelper {
	
	private InternationalizationHelper() {
		super();
	}
	
	public static String formatMessage(final Locale locale, final ResourceBundle rb, final String messageKey, final Object... messageArguments) {
		return InternationalizationHelper.formatMessage(locale, rb.getString(messageKey), messageArguments);
	}
	
	public static String formatMessage(final Locale locale, final String message, final Object... messageArguments) {
		MessageFormat formatter = new MessageFormat("");
		formatter.setLocale(locale);
		formatter.applyPattern(message);
		
		return formatter.format(messageArguments);
	}

}
