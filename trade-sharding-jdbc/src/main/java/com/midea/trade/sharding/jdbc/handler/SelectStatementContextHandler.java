package com.midea.trade.sharding.jdbc.handler;
import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.parser.CursorNode;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.check.Checker;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.NodeAnalyzer;

/**
 * SelectStatementContextHandler
 */
public class SelectStatementContextHandler implements
		StatementContextHandler<CursorNode> {
	
	static final List<Checker> checkers = new ArrayList<Checker>();
	
	
	/**
	 * 1、封装节点树 
	 */
	@Override
	public StatementContext handle(CursorNode statementNode,
			StatementContext context) {
		BatchItem batchItem = context.getCurrentBatch();
		//sql节点树
		batchItem.setStatementTreeNode(statementNode);
		/**
		 * 解析表信息  封装解析sql结果
		 */
		this.parseTableInfo(batchItem, statementNode);
		return context;
	}

	
	
	void parseTableInfo(BatchItem batchItem, CursorNode statementNode) {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers.get(statementNode.getNodeType());
		AnalyzeResult result = analyzer.analyze(statementNode);
		batchItem.setAnalyzeResult(result);
		if (!checkers.isEmpty()) {
			for (Checker checker : checkers) {
				checker.check(result);
			}
		}
	}

}
