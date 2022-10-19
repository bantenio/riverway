package com.yomahub.liteflow.exception;

public class NoAvailableSlotException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/** 异常信息 */
	private String message;

	public NoAvailableSlotException(String message) {
		super(message);
		this.message = message;
	}


	public NoAvailableSlotException(String message, Throwable tx) {
		super(message, tx);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
