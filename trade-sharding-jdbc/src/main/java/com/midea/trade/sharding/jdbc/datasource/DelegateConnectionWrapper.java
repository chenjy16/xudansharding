package com.midea.trade.sharding.jdbc.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import com.midea.trade.sharding.core.context.TransactionContext;
import com.midea.trade.sharding.core.jdbc.ConnectionManager;
import com.midea.trade.sharding.jdbc.ConnectionWrapper;
import com.midea.trade.sharding.jdbc.ProviderDesc;


/**
 * 自定义封装数据源方式的Connection实现
 * 
 */
public class DelegateConnectionWrapper extends ConnectionWrapper {
	
	private boolean autoCommit = true;

	public DelegateConnectionWrapper(Connection connection) {
		super(connection);
	}

	public DelegateConnectionWrapper(Connection connection, ProviderDesc desc) {
		super(connection, null, desc);
	}

	public DelegateConnectionWrapper(Connection connection,
			ConnectionManager manager, ProviderDesc desc) {
		super(connection, manager, desc);
	}
	
	@Override
	public boolean getAutoCommit() throws SQLException {
		return this.autoCommit;
	}
	
	@Override
	public void setAutoCommit(boolean autoCommit) throws SQLException {
		super.setAutoCommit(autoCommit);
		this.autoCommit = autoCommit;
	}
	
	@Override
	public void commit() throws SQLException {
		super.commit();
		TransactionContext.getContext().release();
	}
	
	@Override
	public void rollback() throws SQLException {
		super.rollback();
		TransactionContext.getContext().release();
	}
	
	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		super.rollback(savepoint);
		TransactionContext.getContext().release();
	}

}
