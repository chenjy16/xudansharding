package com.midea.trade.sharding.jdbc.router;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.jdbc.executors.Executor;
import com.midea.trade.sharding.jdbc.executors.ExecutorsBuilder;



/**
 * 对结果进行分组路由: 1.batch 2.prepared 3.普通statement
 */
public class SimpleRouter implements Router {
	
	TargetDispatcher targetDispatcher=new DefaultTargetDispatcher();

	@Override
	public Executor<?> route(StatementContext context) {
		Executor<?> executor = ExecutorsBuilder.build(context);
		for (BatchItem item : context.getBaches()) {
			context.addTargets(targetDispatcher.dispatch(item));
		}
		return executor;

	}

	

}
