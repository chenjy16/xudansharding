package com.midea.trade.sharding.core.jdbc;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementCallback extends StatementCallback{
	
	 PreparedStatement prepareStatement(Connection conn,String sql)throws SQLException;

}
