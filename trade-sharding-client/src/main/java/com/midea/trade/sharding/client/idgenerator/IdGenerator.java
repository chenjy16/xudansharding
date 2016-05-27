package com.midea.trade.sharding.client.idgenerator;

import java.io.Serializable;

/**
 * ID 生成器约束
 */
public interface IdGenerator<T extends Serializable> { 
	
	T generate() ;
	
}
