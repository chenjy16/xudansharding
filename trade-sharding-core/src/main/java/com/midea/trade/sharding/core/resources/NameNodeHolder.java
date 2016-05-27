package com.midea.trade.sharding.core.resources;

import java.util.List;

import com.google.common.base.Objects;

/**
 * NameNode 实例持有者
 */
public class NameNodeHolder implements NameNode {
	
	final NameNode nameNode;
	String orgTableName;
	String tableName;
	String schema;
	int index;

	public NameNodeHolder(NameNode node) {
		this.nameNode = node;
	}
 
	@Override
	public List<DataNodeHolder> getDataNodes() {
		return nameNode.getDataNodes();
	}

	@Override
	public List<DataNodeHolder> getWriteNodes() {
		return nameNode.getWriteNodes();
	}

	@Override
	public List<DataNodeHolder> getReadNodes() {
		return nameNode.getReadNodes();
	}

	@Override
	public DataNodeHolder remove(DataNodeHolder dataNode) {
		return nameNode.remove(dataNode);
	}

	@Override
	public String getId() {
		return nameNode.getId();
	}
	
	@Override
	public LoadBanlance getLoadBanlance() {
		return nameNode.getLoadBanlance();
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getOrgTableName() {
		return orgTableName;
	}

	public void setOrgTableName(String orgTableName) {
		this.orgTableName = orgTableName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("schema", schema)
				.add("orgTableName", orgTableName)
				.add("tableName", tableName)
				.add("index", index)
				.add("nameNode", nameNode.getId())
				.toString();
	}

}
