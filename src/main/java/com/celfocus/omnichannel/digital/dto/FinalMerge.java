package com.celfocus.omnichannel.digital.dto;

import java.util.Map;

public class FinalMerge {
	private Project project;
	private Map<String, String> i18n;
	
	public FinalMerge(Project project) {
		super();
		this.project = project;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
	public Map<String, String> getI18n() {
		return i18n;
	}
	public void setI18n(Map<String, String> i18n) {
		this.i18n = i18n;
	}

}
