package com.midea.trade.sharding.jdbc.executors;
import java.sql.SQLException;
import com.midea.trade.sharding.core.context.StatementContext;

/**
 * 执行器约束
 */
public interface Executor<T> { 
	
    public T execute(StatementContext context,ExecuteCallback<T> callback) throws SQLException;

}
