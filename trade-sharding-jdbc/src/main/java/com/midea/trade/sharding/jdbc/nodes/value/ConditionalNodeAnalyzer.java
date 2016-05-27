package com.midea.trade.sharding.jdbc.nodes.value;

import com.foundationdb.sql.parser.ConditionalNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;

/**
 * SQL节点解析器:ConditionalNodeAnalyzer
 * 
 */
public class ConditionalNodeAnalyzer extends
		AbstractNodeAnalyzer<ConditionalNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.CONDITIONAL_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(ConditionalNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
