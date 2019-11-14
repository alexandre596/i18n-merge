package com.celfocus.omnichannel.digital.io.filter;

import java.io.File;

import org.apache.commons.io.filefilter.NameFileFilter;

public class NameFileUnmatchFilter extends NameFileFilter {

	private static final long serialVersionUID = 7261215496300283802L;

	public NameFileUnmatchFilter(String name) {
		super(name);
	}
	
    @Override
    public boolean accept(final File file) {
    	return !super.accept(file);
    }
    
    @Override
    public boolean accept(final File dir, final String name) {
    	return !super.accept(dir, name);
    }

}
