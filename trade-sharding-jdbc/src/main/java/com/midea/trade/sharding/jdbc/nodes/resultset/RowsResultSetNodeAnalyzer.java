package com.midea.trade.sharding.jdbc.nodes.resultset;

import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.RowsResultSetNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;



/**
 * SQL节点解析器:RowsResultSetNodeAnalyzer
 */
public class RowsResultSetNodeAnalyzer extends
		AbstractNodeAnalyzer<RowsResultSetNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.ROWS_RESULT_SET_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(RowsResultSetNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
