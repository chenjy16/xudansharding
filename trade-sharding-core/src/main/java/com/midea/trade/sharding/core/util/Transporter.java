package com.midea.trade.sharding.core.util;

/**
 * Transporter
 */
public class Transporter<T> {
	
	private T value;
	
	public Transporter(T value) {
		this.value = value;
	}
	
	public void setValue(T value) {
		this.value = value;
	}
	
	public T getValue() {
		return this.value;
	}

}
