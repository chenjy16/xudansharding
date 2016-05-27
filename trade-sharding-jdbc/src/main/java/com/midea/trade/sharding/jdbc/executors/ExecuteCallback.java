package com.midea.trade.sharding.jdbc.executors;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 执行回调约束
 */
public abstract class ExecuteCallback<T> {
	
	public abstract T execute(Statement statement,String sql) 
			throws SQLException;

}
