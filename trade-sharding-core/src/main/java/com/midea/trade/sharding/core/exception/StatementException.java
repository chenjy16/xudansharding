package com.midea.trade.sharding.core.exception;

public class StatementException extends RuntimeException {
	private static final long serialVersionUID = 336662275824829375L;

	public StatementException(String message) {
		super(message);
	}

	public StatementException(String message, Throwable e) {
		super(message, e);
	}
}
