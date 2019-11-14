package com.celfocus.omnichannel.digital.dto;

public class ValueDifference<T> {
	
	private T oldValue;
	private T newValue;
	
	public ValueDifference() {
		super();
	}
	
	public ValueDifference(T oldValue, T newValue) {
		super();
		this.oldValue = oldValue;
		this.newValue = newValue;
	}
	
	public T getOldValue() {
		return oldValue;
	}
	public void setOldValue(T oldValue) {
		this.oldValue = oldValue;
	}
	public T getNewValue() {
		return newValue;
	}
	public void setNewValue(T newValue) {
		this.newValue = newValue;
	}

}
