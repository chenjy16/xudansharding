package com.midea.trade.sharding.config;

import java.util.ArrayList;
import java.util.List;

/**
 * NameNode配置实体
 */
public class NameNodeConfig extends BaseNameNodeConfig implements Config,
		Cloneable {

	private static final long serialVersionUID = 1L;
	
	String id;
	String schema; 
	String loadbalance;
	String ref;
	List<DataNodeReferenceConfig> referenceNodes = new ArrayList<DataNodeReferenceConfig>();
	String tableName;
	String orgTableName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
 

	public String getLoadbalance() {
		return loadbalance;
	}

	public void setLoadbalance(String loadbalance) {
		this.loadbalance = loadbalance;
	}

	public List<DataNodeReferenceConfig> getReferenceNodes() {
		return referenceNodes;
	}

	public void setReferenceNodes(List<DataNodeReferenceConfig> referenceNodes) {
		this.referenceNodes = referenceNodes;
	}

	public void addReferenceNode(DataNodeReferenceConfig node) {
		referenceNodes.add(node);
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
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

	@Override
	public String toString() {
		return "NameNodeConfig [id=" + id + ", loadbalance=" + loadbalance + ", dataNodes="
				+ referenceNodes + "]";
	}

}
