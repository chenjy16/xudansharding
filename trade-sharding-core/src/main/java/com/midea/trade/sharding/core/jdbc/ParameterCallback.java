package com.midea.trade.sharding.core.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 参数回调方法约束
 */
public interface ParameterCallback<T> {
	
	int hashCode();

	int parameterIndex();
	
	void setParameterIndex(int index);

	void call(PreparedStatement preparedStatement) throws SQLException;

	T getParameter(); 
	
	void setParameter(Object object);

}
