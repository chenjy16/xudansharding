package com.midea.trade.sharding.jdbc.handler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.parser.InsertNode;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.RowsResultSetNode;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.jdbc.ParameterCallback;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.NodeAnalyzer;

/**
 * InsertStatementContextHandler
 */
public class InsertStatementContextHandler implements
		StatementContextHandler<InsertNode> {
	static Logger logger = LoggerFactory
			.getLogger(InsertStatementContextHandler.class);

	@Override
	public StatementContext handle(InsertNode statementNode,
			StatementContext context) {

		BatchItem batchItem = context.getCurrentBatch();
	
		if (statementNode.getResultSetNode() instanceof RowsResultSetNode) {
			// insert into users(id, name, age) values(1, 'zhangsan', 20),(2, 'lisi', 19)
			// 这种插入语句在sharding场景中无法保证路由唯一，通过约束要求不予支持
			throw new ShardException("Do not support muti-insert in one sql! SQL:"+batchItem.getSql());
			
			/*
			// 写入多列，先不考虑性能，这里会将所有rows从新拆分，然后按照batch的方式处理
			Collection<ParameterCallback<?>> callbacks = batchItem
					.getCallbacks();
			RowsResultSetNode rowsNode = (RowsResultSetNode) statementNode
					.getResultSetNode();
			List<RowResultSetNode> rows = rowsNode.getRows();
			List<RowResultSetNode> tempRows = new ArrayList<RowResultSetNode>(
					rows);
			context.clearBatch();
			for (int i = 0; i < tempRows.size(); i++) {
				rows.clear();
				rows.add(tempRows.get(i));
				NodeTreeToSql sqlTree = new NodeTreeToSql();
				try {
					String sql = sqlTree.toString(statementNode);
					SQLParser parser = StatementHelper.createSQLParser();
					InsertNode insertNode = (InsertNode) parser
							.parseStatement(sql); 
					batchItem = context.getCurrentBatch();
					batchItem.addAll(callbacks);
					batchItem.setSql(sql);
					batchItem.setStatementTreeNode(insertNode);
					this.parseTableInfo(batchItem, insertNode);
					context.addBatch(sql);

				} catch (Exception e) {
					logger.error("generate sql error!", e);
				}
			}
			*/
		} else {
			/**
			 * 解析表信息
			 */
			this.parseTableInfo(batchItem, statementNode);
			batchItem.setStatementTreeNode(statementNode);
		}

		if (context.getBaches().size() > 1) {
			this.resetCallbacks(context.getBaches());
		}
		
		return context;
	}

	void resetCallbacks(List<BatchItem> batches) {
		int start = 0;
		for (BatchItem batchItem : batches) {
			this.resetCallBack(batchItem, start);
			start += batchItem.getCallbacks().size();
		}
	}

	void resetCallBack(BatchItem batchItem, int start) {
		List<ParameterCallback<?>> callbacks = new ArrayList<ParameterCallback<?>>();
		Collection<TableColumn> columns = batchItem.getAnalyzeResult()
				.getConditionColumns();
		int i = 1;
		for (TableColumn column : columns) {
			if (column.getPreparedIndex() != null) {
				int index = start + column.getPreparedIndex();
				ParameterCallback<?> callback = batchItem.getCallback(index);
				callback.setParameterIndex(i++);
				callbacks.add(callback);
			}
		}
		batchItem.clearCallbacks();
		batchItem.addAll(callbacks);
	}

	void parseTableInfo(BatchItem batchItem, InsertNode statementNode) {

		/**
		 * 获取当前sql的数据库表信息
		 */
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(statementNode.getNodeType());
		AnalyzeResult result = analyzer.analyze(statementNode);
		result.getConditionColumns().addAll(result.getResultColumns());
		batchItem.setAnalyzeResult(result);

	}

	

}
