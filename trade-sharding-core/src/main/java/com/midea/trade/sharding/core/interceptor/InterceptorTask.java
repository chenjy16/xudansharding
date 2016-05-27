package com.midea.trade.sharding.core.interceptor;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.shard.TableInfo;


/**
 * 异步拦截器执行任务
 */
public class InterceptorTask implements Runnable {
	
	Interceptor interceptor;
	TableInfo tableInfo;
	TableColumn[] updatedColumns;
	TableColumn[] conditionColumns;

	public InterceptorTask(Interceptor interceptor, TableInfo tableInfo,
			TableColumn[] updatedColumns, TableColumn[] conditionColumns) {
		this.interceptor = interceptor;
		this.tableInfo = tableInfo;
		this.updatedColumns = updatedColumns;
		this.conditionColumns = conditionColumns;
	}

	@Override
	public void run() {
		interceptor.intercept(tableInfo, updatedColumns, conditionColumns);
	}

}
