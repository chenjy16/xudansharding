package com.midea.trade.sharding.core.jdbc.result;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * RowSet 生成器约束
 */
public interface RowSetCreator {

	public RowSet create(ResultSet resultSet, ColumnFinder columnFinder) throws SQLException;

}
