package com.midea.trade.sharding.core.exception;

/**
 *执行过程异常
 */
public class ExecuteException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public ExecuteException() {
		super(); 
	}

	public ExecuteException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ExecuteException(String message) {
		super(message); 
	}

	public ExecuteException(Throwable cause) {
		super(cause); 
	}

}
