package com.midea.trade.sharding.jdbc.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.midea.trade.sharding.core.context.ConnectionContext;
import com.midea.trade.sharding.core.context.TransactionContext;
import com.midea.trade.sharding.core.jdbc.ConnectionManager;
import com.midea.trade.sharding.core.util.JdbcUtil;
import com.midea.trade.sharding.jdbc.ConnectionWrapper;
import com.midea.trade.sharding.jdbc.ProviderDesc;

/**
 * DatabaseUtils
 */
public class DatabaseUtils {
	
	public static Connection wrapConnection(Connection connection,
			ConnectionManager manager) {
		
		if (connection instanceof ConnectionWrapper) {
			return connection;
		}
		return new ConnectionWrapper(connection, manager, null);
	}

	public static Connection wrapConnection(Connection connection,
			ConnectionManager manager, ProviderDesc providerDesc) {
		if (connection instanceof ConnectionWrapper) {
			return connection;
		}
		return new ConnectionWrapper(connection, manager, providerDesc);
	}

	public static void beginTransaction(Connection connection) throws SQLException {
		ConnectionContext context = ConnectionContext.getContext();
		context.beginTransaction();
		connection.setAutoCommit(false);
	}

	public static void endTransaction(Connection connection) throws SQLException {
		connection.commit();
	}
	
	public static void releaseTransaction() {
		TransactionContext.getContext().release();
	}
	
	public static void closeConnection(Connection conn) {
		JdbcUtil.closeConnection(conn);
	}

	public static void closeStatement(Statement stmt) {
		JdbcUtil.closeStatement(stmt);
	}

	public static void closeResultSet(ResultSet rs) {
		JdbcUtil.closeResultSet(rs);
	}

}
