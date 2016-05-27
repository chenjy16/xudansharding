package com.midea.trade.sharding.core.shard;

import java.util.Map;

/**
 * 分库分表规则约束
 * 通过对sql中的字段的值来判断放在哪个数据库或者表中 
 * 常用的方式有： 
 * 1.基于ID段的 
 * 2.基于hash的 
 * 3.按日期等

 */
public interface Function {
	
	/**
	 *  一个无分库分表规则的funcion
	 */
	public static final Function NO_SHARD_FUNCTION = new NoShardFunction();
	
	/**
	 * 执行方法返回下标
	 * 
	 * @param parameters
	 *            key为字段名，大写开头，value为字段在sql中的值
	 * @return namenode的下标
	 */
	int execute(int size, Map<String, Object> parameters);
	
	/**
	 * 一个无分库分表规则的funcion
	 */
	static class NoShardFunction implements Function {

		@Override
		public int execute(int size, Map<String, Object> parameters) {
			return 0;
		}
		
	}

}
