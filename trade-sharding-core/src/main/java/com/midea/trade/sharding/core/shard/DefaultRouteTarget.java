package com.midea.trade.sharding.core.shard;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.resources.NameNode;


/**
 * 默认路由

 */
public class DefaultRouteTarget implements RouteTarget {
	
	BatchItem batchItem;
	
	NameNode nameNode;
	
	SqlExecuteInfo executeInfo;

	public DefaultRouteTarget(BatchItem batchItem, NameNode nameNode) {
		this.batchItem = batchItem;
		this.nameNode = nameNode;
	}

	public BatchItem getBatchItem() {
		return batchItem;
	}

	public void setBatchItem(BatchItem batchItem) {
		this.batchItem = batchItem;
	}

	public NameNode getNameNode() {
		return nameNode;
	}

	public void setNameNode(NameNode nameNode) {
		this.nameNode = nameNode;
	}

	public SqlExecuteInfo getExecuteInfo() {
		return executeInfo;
	}

	public void setExecuteInfo(SqlExecuteInfo executeInfo) {
		this.executeInfo = executeInfo;
	}

}
