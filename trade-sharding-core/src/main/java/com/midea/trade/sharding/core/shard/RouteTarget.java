package com.midea.trade.sharding.core.shard;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;

import com.midea.trade.sharding.core.resources.NameNode;



/**
 * 路由约束
 */
public interface RouteTarget {
	BatchItem getBatchItem();
	NameNode getNameNode();
	SqlExecuteInfo getExecuteInfo();
}
