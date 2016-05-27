package com.midea.trade.sharding.client.routeinfo;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;

import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.jdbc.builder.DefaultStatementContextBuilder;
import com.midea.trade.sharding.jdbc.builder.StatementContextBuilder;
import com.midea.trade.sharding.jdbc.router.Router;
import com.midea.trade.sharding.jdbc.router.RouterFactory;

public class DefaultRouterInfoService implements RouterInfoService {
	static final StatementContextBuilder builder = new DefaultStatementContextBuilder();

	@Override
	public Map<BatchItem, Set<RouteTarget>> route(String sql)
			throws SQLException {
		try {
			StatementContext context = new StatementContext();
			StatementContext.setContext(context);
			builder.build(sql, context);
			Router router = RouterFactory.createRouter(context);
			router.route(context);

			Map<BatchItem, Set<RouteTarget>> targets = context
					.getExecuteInfosMap();
			return targets;
		} finally {
			StatementContext.setContext(null);
		}
	}
	 

}
