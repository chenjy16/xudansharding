package com.midea.trade.sharding.jdbc.nodes.value;

import com.foundationdb.sql.parser.BinaryLogicalOperatorNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;


/**
 * SQL节点解析器:BinaryLogicalOperatorNodeAnalyzer
 * 
 */
public class BinaryLogicalOperatorNodeAnalyzer extends
		AbstractBinaryOperatorNode<BinaryLogicalOperatorNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.AND_NODE, NodeTypes.OR_NODE,
			NodeTypes.IS_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(BinaryLogicalOperatorNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		QueryTreeNode nextNode = node.getLeftOperand();
		analyzeAndMergeResult(result, nextNode); 
		nextNode = node.getRightOperand();
		analyzeAndMergeResult(result, nextNode); 
		return result;
	}

}
