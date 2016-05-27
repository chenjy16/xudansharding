package com.midea.trade.sharding.jdbc.executors.impl;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import com.midea.trade.sharding.core.tx.Transaction;
import com.midea.trade.sharding.jdbc.executors.ExecuteCallback;


@SuppressWarnings({ "unchecked", "rawtypes" })
public class FutureQueryExecuteCallback extends ExecuteCallback {
	
	final ThreadPoolExecutor threadPool;
	final boolean asyn;
	final ExecuteCallback callback;
	final CyclicBarrier barriar;
	final Transaction trasaction;
	Future future;

	FutureQueryExecuteCallback(ThreadPoolExecutor threadPool, boolean asyn,
			ExecuteCallback callback, CyclicBarrier barriar,
			Transaction trasaction) {
		this.threadPool = threadPool;
		this.asyn = asyn;
		this.callback = callback;
		this.barriar = barriar;
		this.trasaction = trasaction;
	}

	@Override
	public Object execute(final Statement statement, final String sql)
			throws SQLException {
		//只向一个库执行sql
		if (!asyn) {
			return doExecute(statement, sql, callback);

		}
		//向多个库并发的执行sql
		future = threadPool.submit(new Callable() {
			@Override
			public Object call() throws Exception {
				try{
					Object result = doExecute(statement, sql, callback);
					return result;
				}catch(Exception e){
					e.printStackTrace();
					throw e;
				}finally{
					barriar.await(threadPool.getKeepAliveTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
				}
			}
		});

		return null;
	}

	protected final Object doExecute(Statement statement, String sql,
			ExecuteCallback<Integer> callback) throws SQLException {
		Object result = callback.execute(statement, sql);
		return result;
	}
}
