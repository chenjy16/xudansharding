package com.midea.trade.sharding.core.resources;

import java.sql.Connection;

/**
 * Resource
 */
public interface Resource {
	
	Connection getConnection();
	
}
