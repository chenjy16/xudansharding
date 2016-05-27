package com.midea.trade.sharding.jdbc.handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.parser.DeleteNode;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.NodeAnalyzer;


/**
 * DeleteStatementContextHandler
 */
public class DeleteStatementContextHandler implements
		StatementContextHandler<DeleteNode> {
	static Logger logger = LoggerFactory
			.getLogger(DeleteStatementContextHandler.class);

	@Override
	public StatementContext handle(DeleteNode statementNode,
			StatementContext context) {

		BatchItem batchItem = context.getCurrentBatch();
		batchItem.setStatementTreeNode(statementNode);
		/**
		 * 解析表信息
		 */
		this.parseTableInfo(batchItem, statementNode); 
		return context;
	}

	void parseTableInfo(BatchItem batchItem, DeleteNode statementNode) {

		/**
		 * 获取当前sql的数据库表信息
		 */
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(statementNode.getNodeType());
		AnalyzeResult result = analyzer.analyze(statementNode);
		batchItem.setAnalyzeResult(result); 
	}
 
}
