package com.midea.trade.sharding.jdbc.results.merger;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.midea.trade.sharding.core.jdbc.result.ColumnFinder;
import com.midea.trade.sharding.core.jdbc.result.RowSet;
import com.midea.trade.sharding.core.jdbc.result.RowSetCreator;
import com.midea.trade.sharding.core.jdbc.result.SimpleRowSet;

/**
 * DefaultRowSetCreator
 */
public class DefaultRowSetCreator implements RowSetCreator {
	

	@Override
	public RowSet create(ResultSet resultSet, ColumnFinder columnFinder) throws SQLException { 
		int count=resultSet.getMetaData().getColumnCount();
		Object []values=new Object[count];
		for(int i=0;i<values.length;i++){
			values[i]=resultSet.getObject(i+1);
		}
		SimpleRowSet rowSet=new SimpleRowSet(values,columnFinder);
		rowSet.setResultSet(resultSet);
		return rowSet;
	}

}
