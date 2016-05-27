package com.midea.trade.sharding.jdbc.executors;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.jdbc.executors.impl.BatchExecutor;
import com.midea.trade.sharding.jdbc.executors.impl.SimpleExecutor;


/**
 * 执行器构建
 */
@SuppressWarnings("rawtypes")
public class ExecutorsBuilder {

	static final Executor DEFAULT_EXECUTOR = new SimpleExecutor();
	static final Executor BATCH_EXECUTOR = new BatchExecutor();

	public static Executor<?> build(StatementContext context) {
		if (context.isBatch()) {
			return BATCH_EXECUTOR;
		}
		return DEFAULT_EXECUTOR;
	}

}
