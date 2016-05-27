package com.midea.trade.sharding.core.timetracker;

import com.midea.trade.sharding.core.exception.ConfigurationException;



/**
 * 耗时监控点
 */
public enum TrackPoint {
	/**
	 * 从连接池中获取连接
	 */
	GET_CONNECTION,
	
	/**
	 * 一个connection context的生命周期
	 */
	CONNECTION_CONTEXT,
	
	/**
	 * 执行sql
	 */
	EXECUTE_SQL,
	
	/**
	 * 解析sql
	 */
	PARSE_SQL,
	
	/**
	 * 未知类型
	 */
	UNKNOW;
	
	public static TrackPoint parse(String name) {
		if(name!=null){
			for(TrackPoint tmp : values()){
				if(name.toUpperCase().equals(tmp.name()))
					return tmp;
			}
		}
		throw new ConfigurationException("No such TrackPoint type : "+name);
	}
}
