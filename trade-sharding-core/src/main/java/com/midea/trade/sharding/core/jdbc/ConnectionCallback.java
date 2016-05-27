package com.midea.trade.sharding.core.jdbc;
import java.sql.Connection;
import java.sql.SQLException;




public interface ConnectionCallback {
	
    void call(Connection connection) throws SQLException;
	
	void reset(Connection connection)throws SQLException;
	
	ConnectionEvent getEvent();

}
