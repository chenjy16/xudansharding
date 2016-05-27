package com.midea.trade.sharding.jdbc.handler;

import com.foundationdb.sql.parser.QueryTreeNode;

import com.foundationdb.sql.parser.UpdateNode;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.NodeAnalyzer;



/**
 * UpdateStatementContextHandler
 */
public class UpdateStatementContextHandler implements
		StatementContextHandler<UpdateNode> {

	@Override
	public StatementContext handle(UpdateNode statementNode,
			StatementContext context) { 

		BatchItem batchItem = context.getCurrentBatch();
		batchItem.setStatementTreeNode(statementNode);
		/**
		 * 解析表信息
		 */
		this.parseTableInfo(batchItem, statementNode);
		return context;
	}

	void parseTableInfo(BatchItem batchItem, UpdateNode statementNode) {

		/**
		 * 获取当前sql的数据库表信息
		 */
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(statementNode.getNodeType());
		AnalyzeResult result = analyzer.analyze(statementNode);
		batchItem.setAnalyzeResult(result); 
	}
 

}
