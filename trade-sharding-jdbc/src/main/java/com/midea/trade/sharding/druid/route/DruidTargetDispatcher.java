package com.midea.trade.sharding.druid.route;
import java.util.Set;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.jdbc.router.TargetDispatcher;

public class DruidTargetDispatcher implements TargetDispatcher {

	@Override
	public Set<RouteTarget> dispatch(BatchItem batchItem) {
		
		Configurations configurations = Configurations.getInstance();
		return null;
		
	}

}
