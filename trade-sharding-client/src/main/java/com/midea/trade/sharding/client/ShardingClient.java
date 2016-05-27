package com.midea.trade.sharding.client;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.sql.DataSource;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.jdbc.ProviderDesc;
import com.midea.trade.sharding.jdbc.datasource.DataSourceWrapper;
import com.midea.trade.sharding.jdbc.rw.RWConnectionWrapper;
import com.midea.trade.sharding.jdbc.util.DatabaseUtils;

public final class ShardingClient {
	
	private static final AtomicBoolean inited = new AtomicBoolean(false);
	
	private ShardingClient() {}
	
	
	
	public static synchronized boolean inited() {
		if(!inited.compareAndSet(false, true)){
			return true;
		}
		return false;
	}
	
	
	public static synchronized void init(String configuration) {
		if(!inited.compareAndSet(false, true)){
			throw new ConfigurationException("ShardingClient has been inited !");
		}
		Configurations.getInstance().init(configuration);
	}
	
	
	
	public static void checkInit() {
		if(!inited.get()){
			throw new ConfigurationException("ShardingClient need init !");
		}
	}
	
	public static Connection wrapConnection(Connection connection, ProviderDesc providerDesc) {
		checkInit();
		return DatabaseUtils.wrapConnection(connection, null, providerDesc);
	}
	
	public static DataSource wrapDataSource(DataSource dataSource) {
		checkInit();
		return new DataSourceWrapper(dataSource);
	}
	
	public static Connection getConnection() {
		checkInit();
		return DatabaseUtils.wrapConnection(null, null, null);
	}
	
	public static Connection getConnection(ProviderDesc providerDesc) {
		checkInit();
		return new RWConnectionWrapper(null, providerDesc);
	}
	
	public static void beginTransaction(Connection connection) throws SQLException {
		checkInit();
		DatabaseUtils.beginTransaction(connection);
	}

	public static void endTransaction(Connection connection) throws SQLException {
		checkInit();
		DatabaseUtils.endTransaction(connection);
	}
	
	public static void releaseTransaction() {
		checkInit();
		DatabaseUtils.releaseTransaction();
	}
	
	public static void closeConnection(Connection connection) {
		checkInit();
		DatabaseUtils.closeConnection(connection);
	}

	public static void closeStatement(Statement statement) {
		checkInit();
		DatabaseUtils.closeStatement(statement);
	}

	public static void closeResultSet(ResultSet resultSet) {
		checkInit();
		DatabaseUtils.closeResultSet(resultSet);
	}
	
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		checkInit();
		closeResultSet(rs);
		closeStatement(stmt);
		closeConnection(conn);
	}
}
