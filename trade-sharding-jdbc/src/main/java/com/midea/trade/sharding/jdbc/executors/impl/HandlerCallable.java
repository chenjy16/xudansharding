package com.midea.trade.sharding.jdbc.executors.impl;

import java.sql.SQLException;
import java.util.concurrent.Callable;

import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.jdbc.executors.ExecuteCallback;
import com.midea.trade.sharding.jdbc.executors.ExecuteHandler;



@SuppressWarnings("rawtypes")
public class HandlerCallable implements Callable<HandlerResult>, ExecuteHandler {

	ExecuteHandler handler;
	RouteTarget target;
	StatementContext context;

	ExecuteCallback callback;

	public HandlerCallable(ExecuteHandler handler, RouteTarget target,
			StatementContext context, ExecuteCallback callback) {
		this.handler = handler;
		this.target = target;
		this.context = context;
		this.callback = callback;
	}

	@SuppressWarnings("unchecked")
	@Override
	public HandlerResult call() throws Exception {
		try {
			HandlerResult result = new HandlerResult();
			Object v = handler.handle(target, context, callback);
			result.setResult(v);
			return result;
		} finally {

		}
	}

	@Override
	public Object handle(RouteTarget target, StatementContext context,
			ExecuteCallback callback) throws SQLException {
		Object v = handler.handle(target, context, callback);
		return v;
	}

}
