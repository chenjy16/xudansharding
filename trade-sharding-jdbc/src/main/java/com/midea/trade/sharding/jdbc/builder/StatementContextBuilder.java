package com.midea.trade.sharding.jdbc.builder;
import java.sql.SQLException;

import com.midea.trade.sharding.core.context.StatementContext;

/**
 * Statement上下文构建器约束
 * 
 */
public interface StatementContextBuilder {
	
	public StatementContext build(String stmt,StatementContext context) throws SQLException;

}
