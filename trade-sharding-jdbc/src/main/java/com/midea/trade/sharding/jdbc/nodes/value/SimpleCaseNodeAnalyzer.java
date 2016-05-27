package com.midea.trade.sharding.jdbc.nodes.value;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.SimpleCaseNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;



/**
 * SQL节点解析器:SimpleCaseNodeAnalyzer
 * 
 */
public class SimpleCaseNodeAnalyzer extends
		AbstractNodeAnalyzer<SimpleCaseNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.SIMPLE_CASE_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(SimpleCaseNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
