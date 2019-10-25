package com.ionos.domains.contact.model;

public enum StateMachineHeaders {
	OPERATION("operation"), EVENT("event");

	private final String header;

	StateMachineHeaders(String header) {
		this.header = header;
	}

	public String getHeader() {
		return header;
	}
}
