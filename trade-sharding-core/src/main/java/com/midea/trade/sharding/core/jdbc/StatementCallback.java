package com.midea.trade.sharding.core.jdbc;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public interface StatementCallback {
	
	 Statement createStatement(Connection conn) throws SQLException;

}
