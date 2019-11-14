package com.celfocus.omnichannel.digital.dto;

import java.util.Map;

public class MergeStatus {
	
	private Map<String, String> newLines;
	private Map<String, String> removedLines;
	private Map<String, ValueDifference<String>> differences;
	
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
	public Map<String, ValueDifference<String>> getDifferences() {
		return differences;
	}
	public void setDifferences(Map<String, ValueDifference<String>> differences) {
		this.differences = differences;
	}

}
