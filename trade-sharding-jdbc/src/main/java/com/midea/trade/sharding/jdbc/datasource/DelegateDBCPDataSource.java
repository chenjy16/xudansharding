package com.midea.trade.sharding.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.midea.trade.sharding.jdbc.ConnectionWrapper;


/**
 * DelegateDBCPDataSource
 */
public class DelegateDBCPDataSource extends BasicDataSource {

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		return this.getClass().isAssignableFrom(iface);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <T> T unwrap(Class<T> iface) throws SQLException {
		try {
			return (T) this;
		} catch (Exception e) {
			throw new SQLException(e);
		}
	}

	public Connection getConnection() throws SQLException {
		Connection connection = super.getConnection();
		return new ConnectionWrapper(connection, null);
	}

	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection connection = super.getConnection(username, password);
		return new ConnectionWrapper(connection, null);
	}
}
