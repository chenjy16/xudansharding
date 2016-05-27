package com.midea.trade.sharding.core.interceptor.executor;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.exception.ExecuteException;



public interface ExecutorInterceptor {
	
	void intercept(BatchItem batchItem) throws ExecuteException;

}
