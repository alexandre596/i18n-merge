package com.celfocus.omnichannel.digital.services;

import java.util.List;
import java.util.Map;

import com.celfocus.omnichannel.digital.dto.FinalMerge;
import com.celfocus.omnichannel.digital.dto.MergeStatus;
import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.dto.ResolvedMerge;
import com.celfocus.omnichannel.digital.exception.CouldNotLocateCorrectFileException;
import com.celfocus.omnichannel.digital.exception.InvalidFileException;
import com.celfocus.omnichannel.digital.exception.InvalidJsonException;

public interface MergeFilesService {
	
	/**
	 * This method will return the merge status using the local file as the left side
	 * and the production file as the right side of the comparison
	 * @since 1.0.0
	 * @param productionFilePath Path to the production i18n file
	 * @param projects List of project and its local paths
	 * @return A map containing the project as the key and the {@link MergeStatus} as the value, which
	 * 	contains the new lines added in local environment, the changes made locally and in production
	 *  and the removed lines in the local environment. 
	 * @throws InvalidFileException If the production or the local file are invalid
	 * @throws CouldNotLocateCorrectFileException In case the local i18n file is not located
	 * @throws InvalidJsonException In case of an error parsing the i18n file
	 */
	Map<Project, MergeStatus> getMergeStatus(String productionFilePath, List<Project> projects) throws InvalidFileException;
	
	/**
	 * This method will merge the results obtained from the {@link #getMergeStatus(String, List)} method with the options the user selected on screen
	 * @since 1.0.0
	 * @param resolvedMergeMap a map containing the new values that <em>weren't</em> selected by the user to be added on the final file, 
	 * 		the values that were updated and its corresponding value and the values to be removed
	 * @return A {@link List} containing the values that were on the local file merged with the options selected by the user
	 * @throws InvalidFileException If the local file is invalid
	 * @throws CouldNotLocateCorrectFileException In case the local i18n file is not located
	 * @throws InvalidJsonException In case of an error parsing the i18n file
	 */
	List<FinalMerge> doMerge(Map<Project, ResolvedMerge> resolvedMergeMap) throws InvalidFileException;
	
	/**
	 * This method will save the merged result obtained from the {@link #doMerge(Map)} method to the local file
	 * @since 1.0.0
	 * @param finalMerge An object that contains the final merge result that should be saved on a file 
	 * 		and the {@link Project} object, containing all the data needed to save it in a local file
	 * @throws InvalidFileException If the local file is invalid
	 * @throws CouldNotLocateCorrectFileException In case the local i18n file is not located
	 */
	void saveToFile(FinalMerge finalMerge) throws InvalidFileException;

}
