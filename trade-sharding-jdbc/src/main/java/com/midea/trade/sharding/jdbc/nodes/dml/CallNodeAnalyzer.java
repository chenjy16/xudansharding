package com.midea.trade.sharding.jdbc.nodes.dml;
import com.foundationdb.sql.parser.CallStatementNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;



/**
 * SQL节点解析器:CallNodeAnalyzer
 * 
 */
public class CallNodeAnalyzer extends AbstractNodeAnalyzer<CallStatementNode, AnalyzeResult> {
   int[] nodeTypes={NodeTypes.CALL_STATEMENT_NODE};
	

	@Override
	public int[] getNodeTypes() { 
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(CallStatementNode node) {
		// TODO Auto-generated method stub
		return null;
	}

}
