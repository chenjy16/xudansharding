package com.midea.trade.sharding.core.shard;

import java.util.Map;
import java.util.Map.Entry;

import com.midea.trade.sharding.core.util.HashUtils;

/**
 * 按hashcode进行分库分表
 */
public class HashFunction implements Function{

	@Override
	public int execute(int size, Map<String, Object> parameters){ 
			int hashcode = 0;
			for(Entry<String, Object> parameter : parameters.entrySet()) {
				hashcode += HashUtils.GetHashCode(parameter.getValue());
			}
			return Math.abs(hashcode) % size; 
	}
}
