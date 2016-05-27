package com.midea.trade.sharding.core.loadbalance.ha;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.resources.DataNode;

/**
 * 切换约束
 */
public interface Swapable {
	
	DataNode swapAvailable(DataNode dataNode, BatchItem batchItem);

}
