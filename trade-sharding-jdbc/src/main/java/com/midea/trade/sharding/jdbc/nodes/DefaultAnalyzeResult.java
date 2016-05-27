package com.midea.trade.sharding.jdbc.nodes;
import java.util.ArrayList;

import java.util.Collection;
import java.util.List;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.AnalyzerCallback;
import com.midea.trade.sharding.core.shard.HavingInfo;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.shard.TableInfo;

/**
 * 默认解析结果
 */
public class DefaultAnalyzeResult implements AnalyzeResult {
	final Collection<TableInfo> tableInfos = new ArrayList<TableInfo>();
	final Collection<TableColumn> conditionColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> resultColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> aggregateColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> appendResultColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> orderByColumns = new ArrayList<TableColumn>();
	final Collection<TableColumn> groupByColumns = new ArrayList<TableColumn>();
	
	TableInfo shardTables[];
	HavingInfo havingInfo;
	final Object treeNode;
	StatementType statementType;
	SqlValueItem limit;
	SqlValueItem offset;
	boolean distinct;
	List<AnalyzerCallback> analyzerCallbacks = new ArrayList<AnalyzerCallback>();

	public DefaultAnalyzeResult() {
		this(null);
	}

	public DefaultAnalyzeResult(Object treeNode) {
		this.treeNode = treeNode;
	}

	public Collection<TableInfo> getTableInfos() {
		return tableInfos;
	}

	public Collection<TableColumn> getConditionColumns() {
		return conditionColumns;
	}

	public Collection<TableColumn> getResultColumns() {
		return resultColumns;
	}

	public Collection<TableColumn> getAppendResultColumns() {
		return appendResultColumns;
	}

	public Collection<TableColumn> getOrderByColumns() {
		return orderByColumns;
	}

	public Collection<TableColumn> getGroupByColumns() {
		return groupByColumns;
	}
	@Override
	public Collection<TableColumn> getAggregateColumns() { 
		return aggregateColumns;
	}
	public void addTables(Collection<TableInfo> tables) {
		tableInfos.addAll(tables);
	}

	public void addConditionColumns(Collection<TableColumn> columns) {
		conditionColumns.addAll(columns);
	}

	public void addResultColumns(Collection<TableColumn> columns) {
		resultColumns.addAll(columns);
	}

	public void addAppendResultColumns(TableColumn column) {
		appendResultColumns.add(column);
	}

	public void addOrderByColumns(Collection<TableColumn> columns) {
		orderByColumns.addAll(columns);
	}

	public void addGroupByColumns(Collection<TableColumn> columns) {
		groupByColumns.addAll(columns);
	}

	

	public SqlValueItem getLimit() {
		return limit;
	}

	public void setLimit(SqlValueItem limit) {
		this.limit = limit;
	}

	public SqlValueItem getOffset() {
		return offset;
	}

	public void setOffset(SqlValueItem offset) {
		this.offset = offset;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public Object getTreeNode() {
		return treeNode;
	}

	@Override
	public StatementType getStatementType() {
		return statementType;
	}

	public void setStatementType(StatementType statementType) {
		this.statementType = statementType;
	}

	public Collection<AnalyzerCallback> getAnalyzerCallbacks() {
		return analyzerCallbacks;
	}

	public void addAnalyzerCallback(AnalyzerCallback analyzerCallback) {
		analyzerCallbacks.add(analyzerCallback);
	}
	@Override
	public TableInfo[] getShardTables() { 
		return shardTables;
	}
	
	public void setShardTables(TableInfo[] shardTables) {
		this.shardTables = shardTables;
	}
	@Override
	public HavingInfo getHavingInfo() { 
		return havingInfo;
	}


	public void setHavingInfo(HavingInfo havingInfo) {
		this.havingInfo = havingInfo;
	}

	@Override
	public String toString() {
		return "DefaultAnalyzeResult [tableInfos=" + tableInfos
				+ ", conditionColumns=" + conditionColumns + ", resultColumns="
				+ resultColumns + ", orderByColumns=" + orderByColumns
				+ ", groupByColumns=" + groupByColumns + "]";
	}

	
	public AnalyzeResult clone(){
		DefaultAnalyzeResult result=new DefaultAnalyzeResult();
		return null;
		
	}

	

}
