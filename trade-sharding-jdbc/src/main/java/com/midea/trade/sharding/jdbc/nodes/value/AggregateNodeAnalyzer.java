package com.midea.trade.sharding.jdbc.nodes.value;

import com.foundationdb.sql.parser.AggregateNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.NodeHelper;



/**
 * SQL节点解析器:AggregateNodeAnalyzer
 * 
 */
public class AggregateNodeAnalyzer extends
		AbstractNodeAnalyzer<AggregateNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.AGGREGATE_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(AggregateNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		TableColumn column = NodeHelper.parseAggregate(node);
		result.getAggregateColumns().add(column);
		return result;
	}

}
