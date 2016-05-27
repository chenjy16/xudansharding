package com.midea.trade.sharding.jdbc.handler;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.jdbc.ParameterCallback;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.builder.StatementHelper;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.NodeAnalyzer;

/**
 * BatchStatementContextHandler
 */
class BatchStatementContextHandler implements StatementContextHandler<String> {
	static Logger logger=LoggerFactory.getLogger(BatchStatementContextHandler.class);

	@Override
	public StatementContext handle(String sql, StatementContext context) {
		List<BatchItem> batchItems = context.getBaches();
		Iterator<BatchItem> iterator = batchItems.iterator();

		while (iterator.hasNext()) {
			BatchItem batchItem = iterator.next();
			QueryTreeNode statementNode = null;
			try {
				statementNode = StatementHelper.createSQLParser()
						.parseStatement(batchItem.getSql());
				batchItem.setStatementTreeNode(statementNode);
			} catch (StandardException e) {
				throw new ShardException("sql parse error, sql:"+sql, e);
			}
			/**
			 * 解析表信息
			 */
			this.parseTableInfo(batchItem, statementNode);

		}

		return context;
	}

	void parseTableInfo(BatchItem batchItem, QueryTreeNode statementNode) {

		/**
		 * 获取当前sql的数据库表信息
		 */
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
				.get(statementNode);
		AnalyzeResult result = analyzer.analyze(statementNode);
		batchItem.setAnalyzeResult(result);
		if (StatementType.INSERT.equals(result.getStatementType())) {
			result.getConditionColumns().addAll(result.getResultColumns());
		}

		this.setResolveColumnValues(batchItem);
	}


	/**
	 * 解析prepared的值
	 * 
	 * @param iterator
	 * @param batchItem
	 */
	void setResolveColumnValues(BatchItem batchItem) {
		Collection<TableColumn> columns = batchItem.getAnalyzeResult()
				.getConditionColumns();
		for (TableColumn column: columns){
			if (column.getPreparedIndex() != null) {
				ParameterCallback<?> callback = batchItem.getCallback(column
						.getPreparedIndex());
				if (callback == null) {
					logger.error("callback is null!column=" + column);

				}
				Object value = callback.getParameter();
				column.setValue(value);

			}
			if (logger.isDebugEnabled()) {
				logger.debug("resolve value,column="+column.getName()+",value="+column.getValue());
			}
		}

	}
}
