package com.celfocus.omnichannel.digital.services;

import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.exception.BranchFetchException;
import com.celfocus.omnichannel.digital.exception.CouldNotLocateCorrectFileException;
import com.celfocus.omnichannel.digital.exception.GitException;

public interface GitService {
	
	/**
	 * Check if the branch of the local repository matches the <code>git.allowed.branches</code> property defined in the application.properties file. 
	 * This method will run the {@link #getCurrentBranch(Project)} method in order to retrieve the current branch name
	 * @since 1.0.0
	 * @param project An object that contains the name of the project and its path on the local file system
	 * @return Whether or not the application can commit to this branch
	 * @throws BranchFetchException In case the application could not retrieve the local branch information
	 */
	boolean isBranchValid(Project project) throws BranchFetchException;
	
	/**
	 * Get the current branch the project is in
	 * @since 1.0.0
	 * @param project project An object that contains the name of the project and its path on the local file system
	 * @return The name of the current branch
	 * @throws BranchFetchException In case the application could not retrieve the local branch information
	 */
	String getCurrentBranch(Project project) throws BranchFetchException;
	
	/**
	 * Commit and push changed to the remote repository. This method will check if the branch is valid using the {@link #isBranchValid(Project)} method.
	 * @since 1.0.0
	 * @param project An object that contains the name of the project and its path on the local file system
	 * @throws GitException If it fails to connect to a remote repository, commit or push the changes
	 * @throws CouldNotLocateCorrectFileException In case the application could not locate the i18n file
	 */
	void doCommitAndPush(Project project) throws GitException, CouldNotLocateCorrectFileException;
	
}
