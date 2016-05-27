package com.midea.trade.sharding.config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.midea.trade.sharding.core.shard.ShardType;


/**
 * Table配置实体
 */
public class TableConfig implements Config {

	private static final long serialVersionUID = 1L;

	String name;

	String columns[];

	boolean differName;
	FunctionConfig function;
	ShardType shardType;
	String threadPoolId;

	final List<NameNodeReferenceConfig> referenceList = new ArrayList<NameNodeReferenceConfig>();

	public BaseNameNodeConfig getNameNode(int index) {
		NameNodeReferenceConfig reference = referenceList.get(index);
		return Configurations.getInstance().getNameNodeConfig(
				reference.getRef());

	}

	public String getThreadPoolId() {
		return threadPoolId;
	}

	public void setThreadPoolId(String threadPoolId) {
		this.threadPoolId = threadPoolId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String[] getColumns() {
		return columns;
	}

	public void setColumns(String[] columns) {
		this.columns = columns;
	}

	public FunctionConfig getFunction() {
		return function;
	}

	public void setFunction(FunctionConfig function) {
		this.function = function;
	}

	public ShardType getShardType() {
		return shardType;
	}

	public void setShardType(ShardType shardType) {
		this.shardType = shardType;
	}

	public List<NameNodeReferenceConfig> getReferenceList() {
		return referenceList;
	}

	public void addNode(NameNodeReferenceConfig node) {
		referenceList.add(node);
	}

	public void addAllNode(List<NameNodeReferenceConfig> nodeList) {
		referenceList.addAll(nodeList);
	}

	public boolean isDifferName() {
		return differName;
	}

	public void setDifferName(boolean differName) {
		this.differName = differName;
	}

	@Override
	public String toString() {
		return "TableConfig [name=" + name + ", columns="
				+ Arrays.toString(columns) + ", differName=" + differName
				+ ", function=" + function + ", shardType=" + shardType
				+ ", referenceList=" + referenceList + "]";
	}

}
