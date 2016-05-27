package com.midea.trade.sharding.core.loadbalance;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.NameNode;

/**
 * 分发器约束
 */
public interface Dispatcher {
	DataNode dispatch(NameNode nameNode,BatchItem batchItem);

}
