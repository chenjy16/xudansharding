package com.midea.trade.sharding.core.jdbc;

import java.sql.Connection;

public interface ConnectionManager {
	
	 void release(Connection conn);
	 
}
