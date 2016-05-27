package com.midea.trade.sharding.core.jdbc.result;

import java.sql.SQLException;

import com.midea.trade.sharding.core.shard.TableColumn;


/**
 * group by 关键字生成器
 */
public class GroupByKeyGenerator {
	private final TableColumn groupByColumns[];

	public GroupByKeyGenerator(TableColumn groupByColumns[]) {
		this.groupByColumns = groupByColumns;
	}

	public Object getKey(RowSet rowSet) throws SQLException {
		StringBuilder builder = new StringBuilder();
		for (TableColumn column : groupByColumns) {
			builder.append(rowSet.getObject(column.getResultIndex()));
		}
		return builder.toString();
	}
}
