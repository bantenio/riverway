package com.yomahub.liteflow.exception;

public class ConfigErrorException extends RuntimeException {
	public ConfigErrorException() {
	}

	public ConfigErrorException(String message) {
		super(message);
	}

	public ConfigErrorException(String message, Throwable cause) {
		super(message, cause);
	}

	public ConfigErrorException(Throwable cause) {
		super(cause);
	}

	public ConfigErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
