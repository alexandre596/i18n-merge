package com.celfocus.omnichannel.digital.services;

import java.util.List;

import com.celfocus.omnichannel.digital.exception.InvalidFileException;

public interface ProjectService {
	
	/**
	 * Get the projects names from the zip file 
	 * @author Alexandre
	 * @since 1.0.0
	 * @param zipFile an object containing the path to the zipfile to be scanned
	 * @throws InvalidFileException if the file does not exists or is not the correct zip file
	 */
	List<String> getProjectsFromZip(String filePath) throws InvalidFileException;
	
}
