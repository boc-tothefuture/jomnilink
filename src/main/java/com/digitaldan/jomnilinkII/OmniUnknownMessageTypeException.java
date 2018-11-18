package com.digitaldan.jomnilinkII;

public class OmniUnknownMessageTypeException extends Exception {

	private int type;

	public OmniUnknownMessageTypeException(int type) {
		super();
		this.type = type;
	}

	public OmniUnknownMessageTypeException(String message) {
		super(message);
	}

	public int getUnknownMessageType() {
		return type;
	}

}
