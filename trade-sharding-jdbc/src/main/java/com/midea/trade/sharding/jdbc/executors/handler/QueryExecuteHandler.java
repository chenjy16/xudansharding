package com.midea.trade.sharding.jdbc.executors.handler;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import com.midea.trade.sharding.core.context.ConnectionContext;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.TransactionContext;
import com.midea.trade.sharding.core.loadbalance.Dispatcher;
import com.midea.trade.sharding.core.loadbalance.DispatcherFactory;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.NameNode;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerExecutor;
import com.midea.trade.sharding.core.util.Transporter;
import com.midea.trade.sharding.jdbc.executors.ExecuteCallback;
import com.midea.trade.sharding.jdbc.executors.ExecuteHandler;

/**
 * 查询操作执行器
 */
public class QueryExecuteHandler extends BaseExecuteHandler<ResultSet>
		implements ExecuteHandler<ResultSet> {

	
	/**
	 * 1、根据nameNode配置信息决定采用哪种策略，使用数据库
	 * 2、在一次事务中,一个connection中的相同table对应同一个statement,得到statement,
	 * <connection.toString() + target.getNameNode().toString() + target.getBatchItem().sql, statement>
	 * 3、无事务时PreparedStatementCallback 回调conn.prepareStatement(sql);
	 * 4、ExecuteCallback执行sql回调
	 */
	@Override
	public ResultSet handle(RouteTarget target, StatementContext context,
			ExecuteCallback<ResultSet> callback) throws SQLException {
		ResultSet result = null;
		String sql = target.getExecuteInfo().getExecuteSql();
		//路由逻辑  决定读写库
		NameNode nameNode = target.getNameNode();
		//根据nameNode配置信息决定采用哪种策略，使用数据库
		Dispatcher dispatcher = DispatcherFactory.create(nameNode);
		DataNode dataNode = dispatcher.dispatch(nameNode, target.getBatchItem());
        if (logger.isDebugEnabled()) {
            printDebugInfo(target, Arrays.asList(new DataNode[]{dataNode}));
        }
        Statement statement = null;
        if(ConnectionContext.getContext().isTransaction()) {
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
        } else {
        	Connection connection = dataNode.getConnection();
        	initConnection(connection);
        	statement = this.createStatement(connection, target, context);
        }
        
		if (statement instanceof PreparedStatement) {
			this.parameterizedStatement((PreparedStatement) statement, target);
		}
		
		TrackerExecutor.trackBegin(TrackPoint.EXECUTE_SQL,StatementContext.getContext().getCurrentBatch());
		try {
			result = callback.execute(statement, sql);
			return result;
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.EXECUTE_SQL);
		}
	}
	
	

}
