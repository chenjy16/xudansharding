package com.midea.trade.sharding.core.factory;

/**
 * 对象工厂约束
 */
public interface ObjectFactory<T> {
	
	public Object create(T config);

}
