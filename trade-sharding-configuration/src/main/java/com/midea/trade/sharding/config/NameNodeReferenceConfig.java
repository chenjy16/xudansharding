package com.midea.trade.sharding.config;

/**
 * NameNode引用配置
 */
public class NameNodeReferenceConfig {
	
	String orgTableName;
	String tableName;
	String schema;
	String ref;
	int index;

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getOrgTableName() {
		return orgTableName;
	}

	public void setOrgTableName(String orgTableName) {
		this.orgTableName = orgTableName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

}
