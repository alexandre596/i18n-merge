package com.celfocus.omnichannel.digital.io.filter.util;

import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.NameFileFilter;

import com.celfocus.omnichannel.digital.io.filter.NameFileUnmatchFilter;

public class CustomFileFilterUtils {
	
	private CustomFileFilterUtils() {
		super();
	}
	
    /**
     * Returns a filter that returns true if the filename does not matches the specified text.
     *
     * @param name  the filename
     * @return a name checking filter
     * @see NameFileFilter
     */
    public static IOFileFilter unNameFileFilter(final String name) {
        return new NameFileUnmatchFilter(name);
    }

}
