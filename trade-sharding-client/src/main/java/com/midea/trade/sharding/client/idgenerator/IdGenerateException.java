package com.midea.trade.sharding.client.idgenerator;

/**
 * IdGenerateException
 */
public class IdGenerateException extends RuntimeException{ 
	private static final long serialVersionUID = 3850671142560681828L;

	public IdGenerateException() {
		super(); 
	}

	public IdGenerateException(String message, Throwable cause) {
		super(message, cause); 
	}

	public IdGenerateException(String message) {
		super(message); 
	}

	public IdGenerateException(Throwable cause) {
		super(cause); 
	}

}
