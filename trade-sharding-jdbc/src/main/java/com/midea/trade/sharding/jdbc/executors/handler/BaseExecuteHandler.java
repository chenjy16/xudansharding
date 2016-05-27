package com.midea.trade.sharding.jdbc.executors.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midea.trade.sharding.core.context.ConnectionContext;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.jdbc.ConnectionCallback;
import com.midea.trade.sharding.core.jdbc.ParameterCallback;
import com.midea.trade.sharding.core.jdbc.PreparedStatementCallback;
import com.midea.trade.sharding.core.jdbc.StatementCallback;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.NameNodeHolder;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.core.shard.SqlExecuteInfo;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerExecutor;
import com.midea.trade.sharding.core.tx.SimpleTransaction;
import com.midea.trade.sharding.core.tx.Transaction;
import com.midea.trade.sharding.jdbc.executors.ExecuteCallback;
import com.midea.trade.sharding.jdbc.executors.ExecuteHandler;



/**
 * 执行操作基类
 * 
 */
public abstract class BaseExecuteHandler<T> implements ExecuteHandler<T> {
	protected static Logger logger = LoggerFactory
			.getLogger(BaseExecuteHandler.class);

	
	
	protected Integer doExecute(Connection connection, Statement statement,
			String sql, ExecuteCallback<Integer> callback) throws SQLException {
		TrackerExecutor.trackBegin(TrackPoint.EXECUTE_SQL, 
				StatementContext.getContext().getCurrentBatch());
		try {
			Integer result = callback.execute(statement, sql);
			return result;
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.EXECUTE_SQL);
		}
	}

	
	
	protected void initConnection(Connection connection) throws SQLException {
		this.setConnectionProperties(connection);
		this.setTransactionInfo(connection);
	}

	
	
	protected Statement createStatement(Connection connection,
			RouteTarget target, StatementContext context) throws SQLException {
		if(connection == null){
			throw new ShardException("can not match table, sql:"+context.getCurrentBatch().getSql());
		}
		StatementCallback callback = context.getStatementCreateCallback();
		if (callback instanceof PreparedStatementCallback) {
			SqlExecuteInfo shardInfo = target.getExecuteInfo();
			return ((PreparedStatementCallback) callback).prepareStatement(connection, shardInfo.getExecuteSql());
		}
		return callback.createStatement(connection);
	}

	
	
	
	protected void setConnectionProperties(Connection connection)
			throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		context.addConnection(connection);
		if (context.getConnectionCallbacks() != null) {
			Iterator<ConnectionCallback> it = context.getConnectionCallbacks().iterator();
			
			while (it.hasNext()) {
				ConnectionCallback callback = it.next();
				if (callback.getEvent().name().startsWith("CREATE")) {
					callback.call(connection);
					it.remove();
				} else {
					callback.call(connection);
				}
			}
		}
	}

	
	
	
	
	/**
	* @author chejy 
	* @Description: SimpleTransaction
	* @param connection
	* @return void    返回类型
	* @throws
	 */
	protected void setTransactionInfo(Connection connection) {
		ConnectionContext context = ConnectionContext.getContext();
		if (context.getTransaction() != null) {
			Transaction transaction = new SimpleTransaction(connection);
			context.addTransaction(transaction);
		}

	}
	
	
	/**
	* @author chejy 
	* @Description:执行参数设置回调方法
	* @param statetement
	* @param target
	* @throws SQLException
	* @return void    返回类型
	* @throws
	 */
	protected void parameterizedStatement(PreparedStatement statetement,
			RouteTarget target) throws SQLException {
		Collection<ParameterCallback<?>> callbacks = target.getBatchItem().getCallbacks();
		for (ParameterCallback<?> callback : callbacks) {
			callback.call(statetement);
		}
	}

	protected void printDebugInfo(RouteTarget target,List<DataNode> dataNodeList) {
		NameNodeHolder nameNode = (NameNodeHolder) target.getNameNode();
		 logger.debug("execute @node id=[" + nameNode.getId() + "] @DataNodes="+dataNodeList+"@table=["
				+ nameNode.getTableName() + "] index=[" + nameNode.getIndex()
				+ "] sql=" + target.getExecuteInfo().getExecuteSql()); 
	  	//System.out.println(target.getExecuteInfo().getExecuteSql());
		if (target.getBatchItem().getCallbacks().size() > 0) {
			logger.debug(getParametersLog(target));
		}
	}

	protected String getParametersLog(RouteTarget target) {
		StringBuilder sb = new StringBuilder();
		sb.append("parameters:");
		for (ParameterCallback<?> callback : target.getBatchItem()
				.getCallbacks()) {
			sb.append("\nindex=").append(callback.parameterIndex())
					.append(" value=").append(callback.getParameter());
		}
		return sb.toString();
	}
}
