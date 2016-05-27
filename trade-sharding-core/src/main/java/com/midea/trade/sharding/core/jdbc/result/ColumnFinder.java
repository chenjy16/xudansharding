package com.midea.trade.sharding.core.jdbc.result;

import java.sql.SQLException;

/**
 * 查找列约束
 * 
 */
public interface ColumnFinder {
	
	public int findIndex(String columnName) throws SQLException;
	
}
