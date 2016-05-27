package com.midea.trade.sharding.jdbc.nodes.list;

import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.SubqueryList;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;



/**
 * SQL节点解析器:SubqueryListNodeAnalyzer
 */
public class SubqueryListNodeAnalyzer  extends AbstractNodeAnalyzer<SubqueryList, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.SUBQUERY_LIST};

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	} 

	@Override
	public AnalyzeResult doAnalyze(SubqueryList node) {
		// TODO Auto-generated method stub
		return null;
	}

}
