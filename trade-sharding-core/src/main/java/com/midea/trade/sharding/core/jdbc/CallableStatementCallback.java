package com.midea.trade.sharding.core.jdbc;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;

public interface CallableStatementCallback extends StatementCallback{
	
	public CallableStatement prepareCall(Connection conn,String sql)throws SQLException;

}
