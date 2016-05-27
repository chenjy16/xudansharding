package com.midea.trade.sharding.jdbc.executors.impl;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.tx.Transaction;
import com.midea.trade.sharding.jdbc.executors.ExecuteCallback;


@SuppressWarnings({ "unchecked", "rawtypes" })
public class FutureUpdateExecuteCallback extends ExecuteCallback {
	static Logger logger = LoggerFactory
			.getLogger(FutureUpdateExecuteCallback.class);
	final ThreadPoolExecutor threadPool;
	final boolean asyn;
	final ExecuteCallback callback;
	final CyclicBarrier barriar;
	final Transaction trasaction;
	final boolean autoClosed;
	Future future;

	FutureUpdateExecuteCallback(ThreadPoolExecutor threadPool, boolean asyn,
			ExecuteCallback callback, CyclicBarrier barriar,
			Transaction trasaction, boolean autoClosed) {
		this.threadPool = threadPool;
		this.asyn = asyn;
		this.callback = callback;
		this.barriar = barriar;
		this.trasaction = trasaction;
		this.autoClosed = autoClosed;
	}

	@Override
	public Object execute(final Statement statement, final String sql)
			throws SQLException {
		if (!asyn) {
			return doExecute(statement, sql, callback);

		}

		future = threadPool.submit(new Callable() {
			@Override
			public Object call() throws Exception {
				try {
					Object result = doExecute(statement, sql, callback);
					return result;
				} finally {
					barriar.await(threadPool.getKeepAliveTime(TimeUnit.MILLISECONDS), TimeUnit.MILLISECONDS);
				}
			}
		});

		return null;
	}

	protected final Object doExecute(Statement statement, String sql,
			ExecuteCallback<Integer> callback) throws SQLException {
		Object result = null;
		Connection connection = statement.getConnection();
		try {
			result = callback.execute(statement, sql);
			if (trasaction == null) {
				if (!connection.getAutoCommit()) {
					connection.commit();
				}
			}
		} catch (SQLException e) {
			logger.error("execute update error!sql=" + sql, e);
			if (trasaction == null) {
				if (!connection.getAutoCommit()) {
					connection.rollback();
					if (logger.isDebugEnabled()) {
						logger.debug("transaction error!rollback for sql="+sql);
					}
				}
			}
			throw e;
		} finally {
			if (trasaction == null){
				statement.close();
				connection.close();
			}
		}
		return result;
	}
}
