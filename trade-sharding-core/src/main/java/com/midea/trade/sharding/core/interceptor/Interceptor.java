package com.midea.trade.sharding.core.interceptor;

import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.shard.TableInfo;


/**
 * 拦截器约束
 */
public interface Interceptor {
	public void intercept(TableInfo tableInfo, TableColumn[] updatedColumns,
			TableColumn[] conditionColumns);
	
}
