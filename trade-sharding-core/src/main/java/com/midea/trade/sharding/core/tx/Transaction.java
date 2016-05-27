package com.midea.trade.sharding.core.tx;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * Transaction
 */
public interface Transaction {
	
	String BLANK_SAVE_POINT_NAME="_balank_save_point";
	
	public void commit() throws SQLException;
	
	public void rollback() throws SQLException;
	 
	public void rollback(Savepoint savepoint) throws SQLException;
	
	public void releaseSavepoint(Savepoint savepoint) throws SQLException;
	
	public Savepoint setSavepoint(String name) throws SQLException;
	
	public boolean isCommited();

}
