package com.midea.trade.sharding.client.idgenerator;

/**
 * ID生产约束
 */
public interface ValueProducer<T> {
	
	public T produce();

}
