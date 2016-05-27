package com.midea.trade.sharding.client.routeinfo;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.shard.RouteTarget;

public interface RouterInfoService {

	public Map<BatchItem, Set<RouteTarget>> route(String sql) throws SQLException;

}
