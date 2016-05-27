package com.midea.trade.sharding.core.jdbc;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

/**
 * 内聚数据源的Connection生产者
 */
public class DataSourceConnectionProvider implements ConnectionProvider {
	
	DataSource datasource;

	@Override
	public Connection getConnection() throws SQLException { 
		return datasource.getConnection();
	}

	public void setDatasource(DataSource datasource) {
		this.datasource = datasource;
	}

}
