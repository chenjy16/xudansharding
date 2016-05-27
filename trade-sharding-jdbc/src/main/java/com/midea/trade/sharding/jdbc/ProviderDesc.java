package com.midea.trade.sharding.jdbc;

/**
 * ProviderDesc
 */
public interface ProviderDesc {
	
	/**
	 * 指定  namenode id，从匹配到的NameNode中获取链接
	 * @return
	 */
	String getNameNodeId();

}
