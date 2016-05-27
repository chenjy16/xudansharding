package com.midea.trade.sharding.client;
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
import com.midea.trade.sharding.jdbc.datasource.DelegateConnectionWrapper;

public class MybatisDataSource implements DataSource{
	
	DataSource datasource;
	
	public MybatisDataSource(DataSource datasource) {
		this.datasource = datasource;
	}
	
	public MybatisDataSource(Properties properties){
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

	@Override
	public PrintWriter getLogWriter() throws SQLException {
		if(datasource == null){
			throw new ConfigurationException("Logic DataSource in , do not support this function");
		}
		return datasource.getLogWriter();
	}

	@Override
	public void setLogWriter(PrintWriter out) throws SQLException {
		if(datasource == null){
			throw new ConfigurationException("Logic DataSource in , do not support this function");
		}
		datasource.setLogWriter(out);

	}

	@Override
	public void setLoginTimeout(int seconds) throws SQLException {
		if(datasource == null){
			throw new ConfigurationException("Logic DataSource in , do not support this function");
		}
		datasource.setLoginTimeout(seconds);
	}

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

    /**
     * 和mybatis整合重要入口
     */
	@Override
	public Connection getConnection() throws SQLException {
		
		return new DelegateConnectionWrapper(null, new ConnectionManager() {
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
