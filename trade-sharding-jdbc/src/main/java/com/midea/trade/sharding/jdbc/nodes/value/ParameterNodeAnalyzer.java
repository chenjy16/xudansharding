package com.midea.trade.sharding.jdbc.nodes.value;

import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ParameterNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:ParameterNodeAnalyzer
 */
public class ParameterNodeAnalyzer extends
		AbstractNodeAnalyzer<ParameterNode, AnalyzeResult> {
	
	int[] nodeTypes = { NodeTypes.PARAMETER_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(ParameterNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(); 
		this.analyzeAndMergeResult(result, node);
		return result;
	}
}
