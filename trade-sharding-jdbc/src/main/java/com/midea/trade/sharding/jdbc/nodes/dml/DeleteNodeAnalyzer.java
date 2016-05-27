package com.midea.trade.sharding.jdbc.nodes.dml;

import com.foundationdb.sql.parser.DeleteNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:DeleteNodeAnalyzer
 */
public class DeleteNodeAnalyzer extends AbstractNodeAnalyzer<DeleteNode, AnalyzeResult> {
 
	int[] nodeTypes = { NodeTypes.DELETE_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}
 

	@Override
	public AnalyzeResult doAnalyze(DeleteNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(node);
		result.setStatementType(StatementType.DELETE);
		this.analyzeAndMergeResult(result, node.getResultSetNode());
		return result;
	}

}
