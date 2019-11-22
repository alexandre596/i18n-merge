package com.celfocus.omnichannel.digital.helpers;

import java.io.File;
import java.io.IOException;

import org.eclipse.jgit.api.CheckoutCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.exception.GitException;

public final class GitRepositoryHelper {
	
	private GitRepositoryHelper() {
		super();
	}
	
	public static final Repository getRepositoryFromProject(Project p) throws GitException {
		try {
			CheckoutCommand git = Git.open(new File(p.getProjectPath() + "/.git")).checkout();
			return git.getRepository();
		} catch (IOException e) {
			throw new GitException(e);
		}
		
	}

}
