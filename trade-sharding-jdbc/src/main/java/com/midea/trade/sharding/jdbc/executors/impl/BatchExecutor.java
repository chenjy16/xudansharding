package com.midea.trade.sharding.jdbc.executors.impl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ThreadPoolExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.core.context.ConnectionContext;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.context.StatementContext.BatchItem;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.shard.RouteTarget;
import com.midea.trade.sharding.core.shard.TableInfo;
import com.midea.trade.sharding.jdbc.executors.ExecuteCallback;
import com.midea.trade.sharding.jdbc.executors.ExecuteHandler;
import com.midea.trade.sharding.jdbc.executors.Executor;
import com.midea.trade.sharding.jdbc.executors.handler.DeleteExecuteHandler;
import com.midea.trade.sharding.jdbc.executors.handler.InsertExecuteHandler;
import com.midea.trade.sharding.jdbc.executors.handler.QueryExecuteHandler;
import com.midea.trade.sharding.jdbc.executors.handler.UpdateExecuteHandler;


/**
 * 批处理执行器
 * 
 */
@SuppressWarnings("rawtypes")
public class BatchExecutor implements Executor {
	static Logger logger = LoggerFactory.getLogger(BatchExecutor.class);
	static final ExecuteHandler<Integer> deleteHandler = new DeleteExecuteHandler();
	static final ExecuteHandler<Integer> insertHandler = new InsertExecuteHandler();
	static final ExecuteHandler<Integer> updateHandler = new UpdateExecuteHandler();
	static final ExecuteHandler<ResultSet> queryHandler = new QueryExecuteHandler();

	@Override
	public Object execute(StatementContext context, ExecuteCallback callback)
			throws SQLException {
		return doUpdate(context, callback);
	}

	ExecuteHandler<?> getHandler(StatementType statementType) {
		switch (statementType) {
		case SELECT:
			return queryHandler;
		case INSERT:
			return insertHandler;
		case UPDATE:
			return updateHandler;
		case DELETE:
			return deleteHandler;
		default:
			break;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private int[] doUpdate(StatementContext context, ExecuteCallback callback)
			throws SQLException {
		Map<BatchItem, Set<RouteTarget>> batchItems = context.getExecuteInfosMap();
		
		int[] results = new int[batchItems.size()];
		
		Set<Map.Entry<BatchItem, Set<RouteTarget>>> entrySet = batchItems.entrySet();
		int i = 0;
		for (Map.Entry<BatchItem, Set<RouteTarget>> entry : entrySet) {
			StatementType statementType = entry.getKey().getAnalyzeResult()
					.getStatementType();
			ExecuteHandler<Integer> hanlder = (ExecuteHandler<Integer>) getHandler(statementType);
			BatchItem batchItem = entry.getKey();
			Set<RouteTarget> targets = entry.getValue();
			results[i++] = this.doUpdate(hanlder, batchItem, targets, context,
					callback);
		}
		return results;
	}

	@SuppressWarnings("unchecked")
	private int doUpdate(ExecuteHandler<Integer> hanlder, BatchItem batchItem,
			Set<RouteTarget> targets, StatementContext context,
			ExecuteCallback callback) throws SQLException {
		
		
		int sumResult = 0;
		List<FutureUpdateExecuteCallback> futureHolders = new ArrayList<FutureUpdateExecuteCallback>();
		int i = 0;
		int n = (targets.size() + 1);
		CyclicBarrier barrier = new CyclicBarrier(n);
		boolean asyn = (targets.size() > 1);
		boolean autoClosed = (targets.size() > 1);

		for (RouteTarget target : targets) {
			FutureUpdateExecuteCallback futureCallback = this.createCallback(
					target.getBatchItem().getMatchTable(), barrier, callback,
					asyn, autoClosed);
			
			
			futureHolders.add(futureCallback);
			Integer result = hanlder.handle(target, context, futureCallback);
			if (result != null) {
				sumResult += result;
			}
		}
		if (asyn) {
			try {
				barrier.await();
				for (i = 0; i < futureHolders.size(); i++) {
					sumResult += (Integer) futureHolders.get(i).future.get();
				}
			} catch (Exception e) {
				logger.error("asyn execute error!targets=" + targets, e);
				throw new ShardException("asyn execute error!", e);
			}

		}
		return sumResult;
	}

	FutureUpdateExecuteCallback createCallback(TableInfo tableInfo,
			CyclicBarrier barrier, final ExecuteCallback callback,
			boolean asyn, boolean autoClosed) {

		final ThreadPoolExecutor threadPool = Configurations.getInstance()
				.getThreadPool(tableInfo.getName());
		
		FutureUpdateExecuteCallback resultCallback = new FutureUpdateExecuteCallback(
				threadPool, asyn, callback, barrier, ConnectionContext
						.getContext().getTransaction(), autoClosed);
		return resultCallback;
	}
}
