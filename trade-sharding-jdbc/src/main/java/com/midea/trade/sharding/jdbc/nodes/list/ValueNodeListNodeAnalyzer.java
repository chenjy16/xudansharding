package com.midea.trade.sharding.jdbc.nodes.list;

import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ValueNodeList;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;

/**
 * SQL节点解析器:ValueNodeListNodeAnalyzer
 */
public class ValueNodeListNodeAnalyzer extends
		AbstractNodeAnalyzer<ValueNodeList, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.VALUE_NODE_LIST};

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	} 
 

	@Override
	public AnalyzeResult doAnalyze(ValueNodeList node) {
		// TODO Auto-generated method stub
		return null;
	}

}
