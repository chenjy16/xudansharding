package com.midea.trade.sharding.core.loadbalance.ha;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.loadbalance.Dispatcher;
import com.midea.trade.sharding.core.loadbalance.RandomDispatcher;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.NameNode;

/**
 * 分发器：支持HA、随机负载
 */
public class SwapRandomDispatcher extends RandomDispatcher implements Dispatcher, Swapable{
	
	static Logger logger = LoggerFactory.getLogger(SwapRandomDispatcher.class);
	
	@Override
	public DataNode dispatch(NameNode nameNode, BatchItem batchItem) {
		DataNode dataNode = super.dispatch(nameNode, batchItem);
		if(!dataNode.isAlive()){
			DataNode slave = swapAvailable(dataNode, batchItem);
			if(slave!=null)
				return slave;
		}
		return dataNode;
	}

	
	@Override
	public DataNode swapAvailable(DataNode dataNode, BatchItem batchItem) {
		DataNode[] slaves = dataNode.getSlaves();
		DataNode slave = null;
		if(slaves != null && slaves.length>0){
			for(DataNode tmp : slaves){
				if(!tmp.isAlive()){
					continue;
				}
				slave = tmp;
				break;
			}
		}
		if(slave == null){
			logger.error("DataNode <"+dataNode.getId()+"> has no avaliable slaves");
		}
		return slave;
	}

}
