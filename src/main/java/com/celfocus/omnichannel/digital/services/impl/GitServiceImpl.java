package com.celfocus.omnichannel.digital.services.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.celfocus.omnichannel.digital.dto.Project;
import com.celfocus.omnichannel.digital.exception.BranchFetchException;
import com.celfocus.omnichannel.digital.exception.CouldNotLocateCorrectFileException;
import com.celfocus.omnichannel.digital.exception.GitException;
import com.celfocus.omnichannel.digital.exception.InvalidBranchException;
import com.celfocus.omnichannel.digital.helpers.FileHelper;
import com.celfocus.omnichannel.digital.helpers.GitRepositoryHelper;
import com.celfocus.omnichannel.digital.jna.credenumerate.GenericWindowsCredentials;
import com.celfocus.omnichannel.digital.jna.credenumerate.WindowsCredentialManager;
import com.celfocus.omnichannel.digital.services.GitService;

@Service
public class GitServiceImpl implements GitService {

	@Value("#{'${git.allowed.branches}'.split(',')}")
	private List<String> allowedBranches;

	@Value("${i18n.file.name}")
	private String i18nFileName;

	@Value("${exclude.directories}")
	private String excludeDirectories;
	
	@Value("${git.credentials.address}")
	private String gitCredentialsAddress;
	
	private WindowsCredentialManager gcm;
	
	@Autowired
	public GitServiceImpl(WindowsCredentialManager gcm) {
		this.gcm = gcm;
	}

	@Override
	public boolean isBranchValid(Project project) throws BranchFetchException {
		String currentBranch = this.getCurrentBranch(project);
		return !allowedBranches.stream().filter(p -> currentBranch.matches(p.trim())).collect(Collectors.toList()).isEmpty();
	}

	@Override
	public String getCurrentBranch(Project project) throws BranchFetchException {
		try {
			return GitRepositoryHelper.getRepositoryFromProject(project).getBranch();
		} catch (GitException | IOException e) {
			throw new BranchFetchException(e);
		}
	}

	@Override
	public void doCommitAndPush(Project project) throws GitException, CouldNotLocateCorrectFileException {
		if (!this.isBranchValid(project)) {
			throw new InvalidBranchException("It's not allowed to commit to this branch");
		}
		
		Repository repository = GitRepositoryHelper.getRepositoryFromProject(project);

		try (Git git = new Git(repository)) {
			this.gitAdd(git, project);
			this.gitCommit(git);
			this.gitPush(git);
		} catch (FileNotFoundException | NoFilepatternException e) {
			throw new CouldNotLocateCorrectFileException(e);
		} catch (GitAPIException e) {
			throw new GitException(e);
		}
	}
	
	private void gitAdd(Git git, Project project) throws FileNotFoundException, GitAPIException {
		File locali18n = FileHelper.getLocalFile(project, excludeDirectories, i18nFileName);
		git.add().addFilepattern(locali18n.getAbsolutePath().replace(project.getProjectPath() + "\\", "").replace("\\", "/")).call();
	}
	
	private void gitCommit(Git git) throws GitAPIException {
		git.commit().setMessage("Merge i18n").call();
	}
	
	private void gitPush(Git git) throws GitAPIException {
		GenericWindowsCredentials gwc = gcm.getByTargetName(gitCredentialsAddress);
		
		CredentialsProvider cp = new UsernamePasswordCredentialsProvider(gwc.getUsername(), gwc.getPassword());
		git.push().setCredentialsProvider(cp).call();
	}

}
