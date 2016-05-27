package com.midea.trade.sharding.core.context;

/**
 * statement类型
 */
public enum StatementType {
	
	INSERT, 
	
	DELETE, 
	
	UPDATE, 
	
	SELECT,
	
	CALLABLE,
	
	BATCH;

}
