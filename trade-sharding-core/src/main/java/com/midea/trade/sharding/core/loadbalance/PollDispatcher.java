package com.midea.trade.sharding.core.loadbalance;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.DataNodeHolder;
import com.midea.trade.sharding.core.resources.NameNode;

/**
 * 分发器：轮询
 */
public class PollDispatcher implements Dispatcher {
	
	private static AtomicInteger times = new AtomicInteger(0);

	@Override
	public DataNode dispatch(NameNode nameNode, BatchItem batchItem) {
		StatementType statementType = batchItem.getAnalyzeResult()
				.getStatementType();

		DataNode dataNode = null;
		List<DataNodeHolder> dataNodes = null;
		switch (statementType) {
		case SELECT:
			dataNodes = nameNode.getReadNodes();
			if (dataNodes.isEmpty()) {
				dataNodes = nameNode.getDataNodes();
			}
			break;
		case INSERT:
		case UPDATE:
		case DELETE:
			dataNodes = nameNode.getWriteNodes();
			if (dataNodes.isEmpty()) {
				dataNodes = nameNode.getDataNodes();
			}
			break;
		case BATCH:
			break;
		case CALLABLE:
			break;
		default:
			break;
		}
		if(times.get() >= Integer.MAX_VALUE) {
			times.set(Integer.MAX_VALUE % dataNodes.size());
		}
		dataNode = dataNodes.get(times.getAndIncrement() % dataNodes.size());
		return dataNode;
	}
}
