package com.midea.trade.sharding.core.shard;

import java.util.Collection;

/**
 * having 语句约束
 */
public interface HavingInfo {

	Collection<TableColumn> getAggregateColumns();

	String getExpression();

	AnalyzerCallback getCallback();

}
