package com.midea.trade.sharding.jdbc.nodes.resultset;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.SelectNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:SelectNodeAnalyzer
 * 
 */
public class SelectNodeAnalyzer extends
		AbstractNodeAnalyzer<SelectNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.SELECT_NODE };
	

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(SelectNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		QueryTreeNode nextNode = node.getFromList();
		if (nextNode != null) {
			this.analyzeAndMergeResult(result, nextNode);
		}
		nextNode = node.getWhereClause();
		if (nextNode != null) {
			this.analyzeAndMergeResult(result, nextNode);
		}
		
		return result;
	}

	

}
