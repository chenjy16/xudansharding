package com.midea.trade.sharding.jdbc.results.merger;
import com.midea.trade.sharding.core.context.StatementContext;


/**
 * MergerBuilder
 * 
 */
public class MergerBuilder {
	
	static final DefaultResultSetMerger resultSetMerger = new DefaultResultSetMerger();

	public static ResultSetMerger buildResultSetMerger(StatementContext context) {
		return resultSetMerger;
	}

}
