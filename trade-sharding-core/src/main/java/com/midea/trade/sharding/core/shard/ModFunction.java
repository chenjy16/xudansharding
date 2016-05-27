package com.midea.trade.sharding.core.shard;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 按取模方式分库分表
 */
public class ModFunction implements Function {

	@Override
	public int execute(int size, Map<String, Object> parameters) { 
			long count = 0;
			for (Entry<String, Object> parameter : parameters.entrySet()) {
				long objValue =(Long) parameter.getValue();
				count += objValue;
			}
			System.out.println(Math.abs(count));
			System.out.println(size);
			System.out.println((Math.abs(count) % size));
			return Integer.parseInt((Math.abs(count) % size)+"") ;
		 
	}
}
