package com.midea.trade.sharding.core.tx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * SimpleTransaction
 */
public class SimpleTransaction implements Transaction {
	final Connection connection;
	boolean commited;
	public SimpleTransaction(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void commit() throws SQLException {
		connection.commit(); 
		commited=true;
	}
	@Override
	public boolean isCommited() { 
		return commited;
	}
	@Override
	public void rollback() throws SQLException {
		connection.rollback();
	}

	@Override
	public void rollback(Savepoint savepoint) throws SQLException {
		connection.rollback(savepoint);
	}

	@Override
	public void releaseSavepoint(Savepoint savepoint) throws SQLException {
		connection.releaseSavepoint(savepoint);
	}

	@Override
	public Savepoint setSavepoint(String name) throws SQLException {
		return connection.setSavepoint(name);
	}

	public Connection getConnection() {
		return connection;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((connection == null) ? 0 : connection.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleTransaction other = (SimpleTransaction) obj;
		if (connection == null) {
			if (other.connection != null)
				return false;
		} else if (!connection.equals(other.connection)) {
			return false;
		}
		return true;
	}
 

	
}
