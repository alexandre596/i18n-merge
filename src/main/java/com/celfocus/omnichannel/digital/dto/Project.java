package com.celfocus.omnichannel.digital.dto;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class Project {
	
	private String projectName;
	private String projectPath;

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(String projectPath) {
		this.projectPath = projectPath;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}

}
