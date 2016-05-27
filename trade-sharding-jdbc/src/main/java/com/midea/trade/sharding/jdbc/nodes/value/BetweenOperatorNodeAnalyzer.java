package com.midea.trade.sharding.jdbc.nodes.value;

import com.foundationdb.sql.parser.BetweenOperatorNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.SubqueryNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:BetweenOperatorNodeAnalyzer
 */
public class BetweenOperatorNodeAnalyzer extends
		AbstractNodeAnalyzer<BetweenOperatorNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.BETWEEN_OPERATOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(BetweenOperatorNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		QueryTreeNode nextNode = node.getLeftOperand();
		if(nextNode instanceof SubqueryNode){
			analyzeAndMergeResult(result, nextNode);
		}
		nextNode = node.getRightOperandList();
		if(nextNode!=null){
			analyzeAndMergeResult(result, nextNode);
		}
		return result;
	}

}
