package com.ionos.domains.contact.model;

public enum StateMachineHeaders {
	OPERATION("operation"), EVENT("event");

	private final String _header;

	StateMachineHeaders(String header) {
		this._header = header;
	}

	public String header() {
		return _header;
	}
}
