package com.midea.trade.sharding.core.exception;

/**
 *  sharding过程异常
 */
public class ShardException extends RuntimeException {

	
	private static final long serialVersionUID = -3292736406870138633L;

	public ShardException() {
		super(); 
	}

	public ShardException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ShardException(String message) {
		super(message); 
	}

	public ShardException(Throwable cause) {
		super(cause); 
	}

}
