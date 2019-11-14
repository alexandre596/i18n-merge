package com.celfocus.omnichannel.digital.services;

import java.util.List;
import java.util.Map;

import com.celfocus.omnichannel.digital.dto.MergeStatus;
import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.exception.CouldNotLocateCorrectFileException;
import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.exception.InvalidJsonException;

public interface MergeFilesService {
	
	/**
	 * This method will return the merge status using the local file as the left side
	 * and the production file as the right side of the comparison
	 * @param productionFilePath Path to the production i18n file
	 * @param projects List of project and its local paths
	 * @return A map containing the project as the key and the {@link MergeStatus} as the value, which
	 * 	contains the new lines added in local environment, the changes made locally and in production
	 *  and the removed lines in the local environment. 
	 * @throws InvalidFileException If the production file is invalid
	 * @throws CouldNotLocateCorrectFileException In case the local i18n file is not located
	 * @throws InvalidJsonException In case of an error parsing the i18n file
	 */
	Map<Project, MergeStatus> getMergeStatus(String productionFilePath, List<Project> projects) throws InvalidFileException;

}
