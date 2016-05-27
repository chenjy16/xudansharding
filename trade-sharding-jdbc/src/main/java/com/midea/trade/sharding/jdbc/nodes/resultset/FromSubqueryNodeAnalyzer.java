package com.midea.trade.sharding.jdbc.nodes.resultset;

import com.foundationdb.sql.parser.FromSubquery;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:FromSubqueryNodeAnalyzer
 * 
 */
public class FromSubqueryNodeAnalyzer extends
		AbstractNodeAnalyzer<FromSubquery, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.FROM_SUBQUERY };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(FromSubquery node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		this.analyzeAndMergeResult(result, node.getSubquery());
		return result;
	}

}
