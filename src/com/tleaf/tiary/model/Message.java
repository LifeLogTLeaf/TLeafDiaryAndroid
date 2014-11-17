package com.tleaf.tiary.model;

public class Message {
	private String message;
	boolean isMine;
	boolean isStatusMessage;
	private String responseType;
	
	public Message() {
	}
	public Message(String message, boolean isMine) {
		this.message = message;
		this.isMine = isMine;
		this.isStatusMessage = false;
	}

	public Message(String message, boolean isMine, String responseType) {
		this.message = message;
		this.isMine = isMine;
		this.isStatusMessage = false;
		this.responseType = responseType;

	}
	public Message(boolean status, String message) {
		this.message = message;
		this.isMine = false;
		this.isStatusMessage = status;
	}



	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public boolean isMine() {
		return isMine;
	}
	public void setMine(boolean isMine) {
		this.isMine = isMine;
	}
	public boolean isStatusMessage() {
		return isStatusMessage;
	}
	public void setStatusMessage(boolean isStatusMessage) {
		this.isStatusMessage = isStatusMessage;
	}

	public String getResponseType() {
		return responseType;
	}
	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

}
