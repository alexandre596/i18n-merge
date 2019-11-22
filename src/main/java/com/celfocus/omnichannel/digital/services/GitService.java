package com.celfocus.omnichannel.digital.services;

import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.exception.BranchFetchException;
import com.celfocus.omnichannel.digital.exception.CouldNotLocateCorrectFileException;
import com.celfocus.omnichannel.digital.exception.GitException;

public interface GitService {
	
	boolean isBranchValid(Project project) throws BranchFetchException;
	
	String getCurrentBranch(Project project) throws BranchFetchException;
	
	void doCommitAndPush(Project project) throws GitException, CouldNotLocateCorrectFileException;
	
}
