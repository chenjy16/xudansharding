package com.midea.trade.sharding.core.exception;

/**
 * 配置异常
 */
public class ConfigurationException extends RuntimeException { 
	private static final long serialVersionUID = -6070433409882587676L;

	public ConfigurationException() {
		super(); 
	}

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause); 
	}

	public ConfigurationException(String message) {
		super(message); 
	}

	public ConfigurationException(Throwable cause) {
		super(cause); 
	}

}
