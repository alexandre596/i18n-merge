package com.celfocus.omnichannel.digital.dto;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

public class ResolvedMerge {
	
	private Map<String, String> newLines;
	private Map<String, String> removedLines;
	private Map<String, String> updatedLines;
	
	public ResolvedMerge() {
		this.newLines = new HashMap<>();
		this.removedLines = new HashMap<>();
		this.updatedLines = new HashMap<>();
	}
	public Map<String, String> getNewLines() {
		return newLines;
	}
	public void setNewLines(Map<String, String> newLines) {
		this.newLines = newLines;
	}
	public Map<String, String> getRemovedLines() {
		return removedLines;
	}
	public void setRemovedLines(Map<String, String> removedLines) {
		this.removedLines = removedLines;
	}
	public Map<String, String> getUpdatedLines() {
		return updatedLines;
	}
	public void setUpdatedLines(Map<String, String> updatedLines) {
		this.updatedLines = updatedLines;
	}
	
	@Override
	public String toString() {
		return ReflectionToStringBuilder.toString(this);
	}
}
