package com.midea.trade.sharding.jdbc.executors.handler;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.context.ConnectionContext;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.TransactionContext;
import com.midea.trade.sharding.core.jdbc.ConnectionCallback;
import com.midea.trade.sharding.core.jdbc.ParameterCallback;
import com.midea.trade.sharding.core.jdbc.PreparedStatementCallback;
import com.midea.trade.sharding.core.jdbc.StatementCallback;
import com.midea.trade.sharding.core.loadbalance.Dispatcher;
import com.midea.trade.sharding.core.loadbalance.DispatcherFactory;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.NameNode;
import com.midea.trade.sharding.core.resources.NameNodeHolder;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.core.shard.SqlExecuteInfo;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerExecutor;
import com.midea.trade.sharding.core.tx.SimpleTransaction;
import com.midea.trade.sharding.core.tx.Transaction;
import com.midea.trade.sharding.core.util.Transporter;
import com.midea.trade.sharding.jdbc.executors.ExecuteCallback;
import com.midea.trade.sharding.jdbc.executors.ExecuteHandler;



/**
 * 可写操作执行基类
 * preparedStatementWrapper和connectionWrapper的所有回调方法 都在此执行 通过context方式带过来
 */
public abstract class WritableExecuteHandler implements ExecuteHandler<Integer> {
	protected static Logger logger = LoggerFactory
			.getLogger(WritableExecuteHandler.class);

	@Override
	public Integer handle(RouteTarget target, StatementContext context,
			ExecuteCallback<Integer> callback) throws SQLException {

		NameNode nameNode = target.getNameNode();
		Dispatcher dispatcher = DispatcherFactory.create(nameNode);
		DataNode dataNode = dispatcher.dispatch(nameNode, target.getBatchItem());
		if (logger.isDebugEnabled()) {
			printDebugInfo(target, Arrays.asList(new DataNode[] { dataNode }));
		}
		
		Statement statement = null;
		
		if(ConnectionContext.getContext().isTransaction()) {//有没有指定事务
			TransactionContext transactionContext = TransactionContext.getContext();
			Transporter<Boolean> isNewConn = new Transporter<Boolean>(false);
			Connection connection = transactionContext.getTransactionConnection(dataNode, isNewConn);
			if(isNewConn.getValue()){
				initConnection(connection);
			}	
			statement = transactionContext.getTransactionStatement(connection, target);
			if(statement == null || statement.isClosed()){
				statement = this.createStatement(connection, target, context);
				TransactionContext.getContext().setTransactionStatement(connection, target, statement);
			}
		} else {//根据配置的datanode数据源来执行,此处可以添加连接到数据源!!!!!
			Connection connection = dataNode.getConnection();
			initConnection(connection);
			statement = this.createStatement(connection, target, context);
		}
		if (statement instanceof PreparedStatement) {
			this.parameterizedStatement((PreparedStatement) statement, target);
		}
		String sql = target.getExecuteInfo().getExecuteSql();
		return this.doExecute(statement, sql, callback);
	}

	
	protected Integer doExecute(Statement statement, String sql,
			ExecuteCallback<Integer> callback) throws SQLException {
		
		TrackerExecutor.trackBegin(TrackPoint.EXECUTE_SQL, 
				StatementContext.getContext().getCurrentBatch());
		
		try {
			Integer result = callback.execute(statement, sql);
			return result;
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.EXECUTE_SQL);
		}
	}

	
	/**
	* @author chejy 
	* @Description: StatementCallback执行回调
	* @param connection
	* @param target
	* @param context
	* @return
	* @throws SQLException
	* @return Statement    返回类型
	* @throws
	 */
	protected Statement createStatement(Connection connection,
			RouteTarget target, StatementContext context) throws SQLException {
		
		StatementCallback callback = context.getStatementCreateCallback();
		if (callback instanceof PreparedStatementCallback) {
			SqlExecuteInfo shardInfo = target.getExecuteInfo();
			return ((PreparedStatementCallback) callback).prepareStatement(connection, shardInfo.getExecuteSql());
		}
		return callback.createStatement(connection);
	}

	protected void initConnection(Connection connection) throws SQLException {
		this.setConnectionProperties(connection);
		this.setTransactionInfo(connection);
		
	}

	
	/**
	* @author chejy 
	* @Description: 此处回调连接对象
	* @param connection
	* @throws SQLException
	* @return void    返回类型
	* @throws
	 */
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

	
	protected void setTransactionInfo(Connection connection) {
		ConnectionContext context = ConnectionContext.getContext();
		if (context.getTransaction() != null) {
			Transaction transaction = new SimpleTransaction(connection);
			context.addTransaction(transaction);
		}

	}

	protected void parameterizedStatement(PreparedStatement statetement,
			RouteTarget target) throws SQLException {
		Collection<ParameterCallback<?>> callbacks = target.getBatchItem()
				.getCallbacks();
		for (ParameterCallback<?> callback : callbacks) {
			callback.call(statetement);
		}
	}

	protected void printDebugInfo(RouteTarget target,
			List<DataNode> dataNodeList) {
		NameNodeHolder nameNode = (NameNodeHolder) target.getNameNode();

		logger.debug("execute @node id=[" + nameNode.getId() + "] @DataNodes="
				+ dataNodeList + "@table=[" + nameNode.getTableName()
				+ "] index=[" + nameNode.getIndex() + "] sql="
				+ target.getExecuteInfo().getExecuteSql());
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
