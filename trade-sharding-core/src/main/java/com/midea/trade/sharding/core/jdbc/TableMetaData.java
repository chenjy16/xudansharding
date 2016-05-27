package com.midea.trade.sharding.core.jdbc;

import com.midea.trade.sharding.core.shard.TableColumn;



/**
 * TableMetaData
 */
public class TableMetaData {
	String schema;
	TableColumn id;
	TableColumn columns[];
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public TableColumn getId() {
		return id;
	}
	public void setId(TableColumn id) {
		this.id = id;
	}
	public TableColumn[] getColumns() {
		return columns;
	}
	public void setColumns(TableColumn[] columns) {
		this.columns = columns;
	}
	
	

}
