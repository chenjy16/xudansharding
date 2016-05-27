package com.midea.trade.sharding.core.shard;

import java.util.Collection;
import com.midea.trade.sharding.core.context.StatementType;



/**
 * 解析结果约束
 */
public interface AnalyzeResult extends Cloneable{
	
	public StatementType getStatementType();

	public Object getTreeNode();

	public Collection<TableInfo> getTableInfos();
	
	public TableInfo[] getShardTables();

	/**
	 * where 中的条件语句 或者join的条件语句
	 * 
	 * @return
	 */
	public Collection<TableColumn> getConditionColumns();

	public Collection<TableColumn> getResultColumns();
	
	public Collection<TableColumn> getAppendResultColumns();

	public Collection<TableColumn> getOrderByColumns();

	public Collection<TableColumn> getGroupByColumns();
	
	public Collection<TableColumn> getAggregateColumns();

	public SqlValueItem getLimit();

	public SqlValueItem getOffset();

	public boolean isDistinct();
	
	Collection<AnalyzerCallback> getAnalyzerCallbacks();
	
	public HavingInfo getHavingInfo();
	
	
	public class SqlValueItem{
		Integer value;
		
		int parameterIndex;

		public Integer getValue() {
			return value;
		}

		public void setValue(Integer value) {
			this.value = value;
		}

		public int getParameterIndex() {
			return parameterIndex;
		}

		public void setParameterIndex(int parameterIndex) {
			this.parameterIndex = parameterIndex;
		}
		
	}

}
