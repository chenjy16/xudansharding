package com.midea.trade.sharding.jdbc.results.merger;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.midea.trade.sharding.core.context.StatementContext;


/**
 * ResultSetMerger
 */
public interface ResultSetMerger {
	
	public ResultSet merge(ResultSet []resultSet,StatementContext context) throws SQLException;

}
