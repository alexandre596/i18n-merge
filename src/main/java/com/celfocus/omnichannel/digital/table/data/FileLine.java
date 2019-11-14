package com.celfocus.omnichannel.digital.table.data;

public class FileLine {
	
	private String value;
	private Boolean selected;
	
	public FileLine(String value, Boolean selected) {
		super();
		this.value = value;
		this.selected = selected;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public Boolean getSelected() {
		return selected;
	}
	public void setSelected(Boolean selected) {
		this.selected = selected;
	}
	

}
