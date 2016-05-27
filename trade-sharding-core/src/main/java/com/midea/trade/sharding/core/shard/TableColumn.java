package com.midea.trade.sharding.core.shard;

/**
 * 表列信息
 */
public class TableColumn {
	
	String schema;
	
	String table;
	
	String name;

	String aliasName;

	int resultIndex;

	boolean distinct;
	OrderByType orderByType;

	Integer preparedIndex;
	String aggregate;
	Object aggregateNode;
	String aggregateNodeContent;
	/**
	 * 在执行过程中的值,sql解析中持有的值,in的话会一个一个拆分
	 */
	Object value;

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getTable() {
		return table;
	}

	public String getAliasName() {
		return aliasName;
	}

	public void setAliasName(String aliasName) {
		this.aliasName = aliasName;
	}

	public Integer getPreparedIndex() {
		return preparedIndex;
	}

	public void setPreparedIndex(Integer preparedIndex) {
		this.preparedIndex = preparedIndex;
	}

	public int getResultIndex() {
		return resultIndex;
	}

	public void setResultIndex(int resultIndex) {
		this.resultIndex = resultIndex;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public OrderByType getOrderByType() {
		return orderByType;
	}

	public void setOrderByType(OrderByType orderByType) {
		this.orderByType = orderByType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public boolean isDistinct() {
		return distinct;
	}

	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getAggregate() {
		return aggregate;
	}

	public void setAggregate(String aggregate) {
		this.aggregate = aggregate;
	}

	public Object getAggregateNode() {
		return aggregateNode;
	}

	public void setAggregateNode(Object aggregateNode) {
		this.aggregateNode = aggregateNode;
	}

	public String getAggregateNodeContent() {
		return aggregateNodeContent;
	}

	public void setAggregateNodeContent(String aggregateNodeContent) {
		aggregateNodeContent=aggregateNodeContent.replaceAll("[(]", "\\[(]");
		aggregateNodeContent=aggregateNodeContent.replaceAll("[)]", "\\[)]");
		this.aggregateNodeContent = aggregateNodeContent;
		
	}

	@Override
	public String toString() {
		return "TableColumn [schema=" + schema + ", table=" + table + ", name="
				+ name + ", aliasName=" + aliasName + ", resultIndex="
				+ resultIndex + ", distinct=" + distinct + ", orderByType="
				+ orderByType + ", preparedIndex=" + preparedIndex + ", value="
				+ value + "]";
	}

}
