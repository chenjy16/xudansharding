package com.midea.trade.sharding.jdbc.router;
import java.util.Set;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.shard.RouteTarget;

/**
 * 对结果进行分组路由: 1.batch 2.prepared 3.普通statement
 * 
 */
public interface TargetDispatcher {
	
	Set<RouteTarget> dispatch(BatchItem item);
	
}
