package com.midea.trade.sharding.core.jdbc.aggregate;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.midea.trade.sharding.core.jdbc.result.RowSet;


/**
 * 聚集函数约束
 */
public interface Aggregate<T> {
	String ALL_KEY_PREFIX = "_aggregate.all.";
	
	public int resultIndex();

	public String key();

	public String getColumnName();

	public void addRow(ResultSet result, RowSet item) throws SQLException;

	T value() throws SQLException;

}
