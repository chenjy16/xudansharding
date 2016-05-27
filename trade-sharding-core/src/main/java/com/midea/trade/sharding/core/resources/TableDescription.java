package com.midea.trade.sharding.core.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

import com.midea.trade.sharding.core.interceptor.InterceptorRef;
import com.midea.trade.sharding.core.shard.Function;
import com.midea.trade.sharding.core.shard.ShardType;

/**
 * Table 描述实例
 */
public class TableDescription {
	
	final String tableName;
	
	final List<NameNodeHolder> nameNodes = new ArrayList<NameNodeHolder>();
	
	final List<InterceptorRef> interceptors = new ArrayList<InterceptorRef>();
	
	Function function;
	
	String columns[];
	
	ShardType shardType;
	
	boolean differentName;
	
	ThreadPoolExecutor threadPool;

	public TableDescription(String tableName) {
		this.tableName = tableName;
	}

	public NameNodeHolder getNameNode(int i) {
		return (NameNodeHolder) nameNodes.get(i);
	}

	public List<NameNodeHolder> getNameNodes() {
		return nameNodes;
	}

	public void addNameNode(NameNodeHolder nameNode) {
		nameNodes.add(nameNode);
	}

	public String getTableName() {
		return tableName;
	}

	public Function getFunction() {
		return function;
	}

	public void setFunction(Function function) {
		this.function = function;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public ShardType getShardType() {
		return shardType;
	}

	public void setShardType(ShardType shardType) {
		this.shardType = shardType;
	}

	public boolean isDifferentName() {
		return differentName;
	}

	public void setDifferentName(boolean differentName) {
		this.differentName = differentName;
	}

	public List<InterceptorRef> getInterceptors() {
		return interceptors;
	}

	public ThreadPoolExecutor getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ThreadPoolExecutor threadPool) {
		this.threadPool = threadPool;
	}

	public NameNodeHolder findNameNodeHolder(String tableName) {
		for (NameNodeHolder holder : nameNodes) {
			if (holder.getTableName() != null
					&& holder.getTableName().equalsIgnoreCase(tableName)) {
				return holder;
			}
		}
		return null;
	}

}
