package com.midea.trade.sharding.jdbc.builder;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.DMLStatementNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.SQLParser;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.jdbc.ParameterCallback;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.ShardQueryGenerator;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerExecutor;
import com.midea.trade.sharding.jdbc.handler.HandlerFactory;
import com.midea.trade.sharding.jdbc.handler.StatementContextHandler;
import com.midea.trade.sharding.jdbc.unparse.DefaultShardQueryGenerator;
import com.midea.trade.sharding.jdbc.unparse.LimitAggregateQueryGenerator;

/**
 * 默认Statement上下文构建器
 */
public class DefaultStatementContextBuilder implements StatementContextBuilder {
	
	static Logger logger = LoggerFactory.getLogger(DefaultStatementContextBuilder.class);
	static final ShardQueryGenerator generator = new DefaultShardQueryGenerator();
	static final ShardQueryGenerator limitAvgGenerator = new LimitAggregateQueryGenerator();

	
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public StatementContext build(String sql, StatementContext context)
			throws SQLException {
		if (context == null) {
			context = new StatementContext();
			StatementContext.setContext(context);
			if (logger.isDebugEnabled()) {
				logger.debug("create context!sql=" + sql);
			}
		}
		//存储sql
		if (context.getCurrentBatch().getSql() == null) {
			context.getCurrentBatch().setSql(sql);
		}
		StatementContextHandler handler = null;
		if (context.isBatch()) {
			handler = HandlerFactory.create(StatementType.BATCH);
			StatementContext resultContext = handler.handle(sql, context);
			processPreparedValues(resultContext);
			return resultContext;
		}
		TrackerExecutor.trackBegin(TrackPoint.PARSE_SQL, sql);
		SQLParser parser = StatementHelper.createSQLParser();
		try {
			//解析器
			DMLStatementNode statementNode = (DMLStatementNode) parser
					.parseStatement(sql);
			
			switch (statementNode.getNodeType()) {
			case NodeTypes.CURSOR_NODE:
				handler = HandlerFactory.create(StatementType.SELECT);
				break;
			case NodeTypes.DELETE_NODE:
				handler = HandlerFactory.create(StatementType.DELETE);
				break;
			case NodeTypes.UPDATE_NODE:
				handler = HandlerFactory.create(StatementType.UPDATE);
				break;
			case NodeTypes.INSERT_NODE:
				handler = HandlerFactory.create(StatementType.INSERT);
				break;
			case NodeTypes.CALL_STATEMENT_NODE:
				handler = HandlerFactory.create(StatementType.CALLABLE);
				break;
			}
			//context里面设置 sql节点树和sql解析结果
			StatementContext resultContext = handler.handle(statementNode,context);
			
			TrackerExecutor.trackEnd(TrackPoint.PARSE_SQL);
			//where 后面条件列的值设置
			processPreparedValues(resultContext);
			return resultContext;

		} catch (StandardException se) {
			se.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		processPreparedValues(context);
		return context;
	}

	private void processPreparedValues(StatementContext context) {
		Statement statement = context.getStatementWrapper();
		if (statement instanceof PreparedStatement) {
			for (BatchItem batchItem : context.getBaches()) {
				this.setResolveColumnValues(batchItem,batchItem.getAnalyzeResult());
			}

		}
	}

	/**
	 * 解析prepared的值
	 * @param iterator
	 * @param batchItem
	 */
	void setResolveColumnValues(BatchItem batchItem, AnalyzeResult analyzeResult) {
		
		//where 后面条件列
		Collection<TableColumn> columns = analyzeResult.getConditionColumns();
		
		for (TableColumn column : columns) {
			if (column.getPreparedIndex() != null) {
				ParameterCallback<?> callback = batchItem.getCallback(column.getPreparedIndex());
				if (callback == null) {
					logger.error("callback is null!column=" + column);

				}
				Object value = callback.getParameter();
				column.setValue(value);

			}
			if (logger.isDebugEnabled()) {
				logger.debug("resolve value,column=" + column.getName()
						+ ",value=" + column.getValue());
			}
		}

	}

}
