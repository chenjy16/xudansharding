package com.midea.trade.sharding.jdbc.handler;

import com.midea.trade.sharding.core.context.StatementType;


/**
 * HandlerFactory
 */
public class HandlerFactory {
	static StatementContextHandler<?>[] handlersArr = new StatementContextHandler[StatementType
			.values().length];
	static {
		handlersArr[StatementType.DELETE.ordinal()] = new DeleteStatementContextHandler();
		handlersArr[StatementType.SELECT.ordinal()] = new SelectStatementContextHandler();
		handlersArr[StatementType.INSERT.ordinal()] = new InsertStatementContextHandler();
		handlersArr[StatementType.UPDATE.ordinal()] = new UpdateStatementContextHandler();
		handlersArr[StatementType.CALLABLE.ordinal()] = new CallableStatementContextHandler();
		handlersArr[StatementType.BATCH.ordinal()] = new BatchStatementContextHandler();
	}

	public static StatementContextHandler<?> create(StatementType type) {
		return handlersArr[type.ordinal()];
	}

	public static void main(String args[]) {
		for (StatementType type : StatementType.values()) {
			StatementContextHandler<?> handler = HandlerFactory.create(type);
			System.out.println(type + "=" + handler.getClass());
		}
	}
}
