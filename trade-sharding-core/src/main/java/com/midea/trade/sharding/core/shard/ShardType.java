package com.midea.trade.sharding.core.shard;


/**
 * sharding 类型
 */
public enum ShardType {
	/**
	 * 按数据库进行划分,多库多表
	 */
	BY_DATABASE("BY-DATABASE"),
	/**
	 * 一个schema下面多个表
	 */
	BY_TABLE("BY-TABLE"),
	BY_DATABASE_TABLE("BY-DATABASE-TABLE"),
	NO_SHARD("NO-SHARD");

	private String input;
	
	private ShardType(String input) {
		this.input = input;
	}
	
	public static ShardType parse(String input) {
		for(ShardType shardType : values()){
			if(shardType.input.equals(input) 
					|| shardType.name().equals(input)){
				
				return shardType;
			}
		}
		throw new IllegalArgumentException("Unsupport ShardType, input string : " + input);
	}
}
