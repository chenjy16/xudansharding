package com.midea.trade.sharding.jdbc.handler;

import com.midea.trade.sharding.core.context.StatementContext;


/**
 * StatementContextHandler
 * 
 */
public interface StatementContextHandler<T> {

	public StatementContext handle(T stmt, StatementContext context);

}
