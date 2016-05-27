package com.midea.trade.sharding.core.loadbalance;

import java.util.List;

import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.DataNodeHolder;
import com.midea.trade.sharding.core.resources.NameNode;

/**
 * 分发器：带有权重的轮询
 */
public class PollWeightDispatcher implements Dispatcher{
	
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
		//排序，取出权重最高的一个dataNode  需要在取之后，改变weight的值，改变下次获取的排序结果
		long weight = 0;
		int selected = 0;
		for(int i = 0; i < dataNodes.size(); i++) {	
			if(dataNodes.get(i).getWeight() > weight) {
				weight = dataNodes.get(i).getWeight();
				selected = i;
			}
		}
		dataNode = dataNodes.get(selected);
		return dataNode;
	}
}
