package com.midea.trade.sharding.jdbc.router;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.jdbc.executors.Executor;


/**
 * 路由约束
 */
public interface Router {
	
	public Executor<?> route(StatementContext context);

}
