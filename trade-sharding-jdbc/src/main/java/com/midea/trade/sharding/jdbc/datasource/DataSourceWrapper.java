package com.midea.trade.sharding.jdbc.datasource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;

import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.jdbc.ConnectionManager;
import com.midea.trade.sharding.core.util.BeanUtils;

/**
 * 另外一种自定义封装数据源方式的实现
 */
public class DataSourceWrapper implements DataSource {
	
	DataSource datasource;
	

	public DataSourceWrapper() {
		
	}

	public DataSourceWrapper(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public DataSourceWrapper(Properties properties){
		BasicDataSource dbcpDataSource = new BasicDataSource();
		for (Map.Entry<Object, Object> entry : properties .entrySet()) {
			try {
				BeanUtils.setProperty(dbcpDataSource,
						entry.getKey().toString(), entry.getValue().toString());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		this.datasource = dbcpDataSource;
	}

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#getLogWriter()
	 */
	@Override
	public PrintWriter getLogWriter() throws SQLException {
		if(datasource == null){
			throw new ConfigurationException("Logic DataSource in , do not support this function");
		}
		return datasource.getLogWriter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#setLogWriter(java.io.PrintWriter)
	 */
	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		if(datasource == null){
			throw new ConfigurationException("Logic DataSource in , do not support this function");
		}
		datasource.setLogWriter(out);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.sql.CommonDataSource#setLoginTimeout(int)
	 */
	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		if(datasource == null){
			throw new ConfigurationException("Logic DataSource in , do not support this function");
		}
		datasource.setLoginTimeout(seconds);
	}

	/*
	 * (non-Javadoc)
	 * @see javax.sql.CommonDataSource#getLoginTimeout()
	 */
	@Override
	public int getLoginTimeout() throws SQLException {
		if(datasource == null){
			throw new ConfigurationException("Logic DataSource in , do not support this function");
		}
		return datasource.getLoginTimeout();
	}

    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return null;
    }

    /*
     * (non-Javadoc)
     *
     * @see javax.sql.DataSource#getConnection()
     */
	@Override
	public Connection getConnection() throws SQLException {
		Connection connection = null;
		if(datasource != null){
			connection = datasource.getConnection();
		}
		return new DelegateConnectionWrapper(connection, new ConnectionManager() {
			
			@Override
			public void release(Connection conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}, null);
	}

	
	/*
	 * (non-Javadoc)
	 * @see javax.sql.DataSource#getConnection(java.lang.String,
	 * java.lang.String)
	 */
	@Override
	public Connection getConnection(String username, String password)
			throws SQLException {
		Connection connection = null;
		if(datasource != null){
			datasource.getConnection(username, password);
		}
		return new DelegateConnectionWrapper(connection,null);
	}

}
