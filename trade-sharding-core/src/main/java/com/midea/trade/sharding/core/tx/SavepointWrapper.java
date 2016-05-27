package com.midea.trade.sharding.core.tx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * SavepointWrapper
 */
public class SavepointWrapper implements Savepoint {

	Savepoint wrapper;
	@Override
	public int getSavepointId() throws SQLException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSavepointName() throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Connection getConnection(){
		return null;
	}

}
