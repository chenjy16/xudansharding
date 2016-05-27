package com.midea.trade.sharding.jdbc.executors.impl;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
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
import com.midea.trade.sharding.jdbc.results.merger.MergerBuilder;



@SuppressWarnings({ "rawtypes", "unchecked" })
public class SimpleExecutor implements Executor {
	static Logger logger = LoggerFactory.getLogger(SimpleExecutor.class);
	static final ExecuteHandler<Integer> deleteHandler = new DeleteExecuteHandler();
	static final ExecuteHandler<Integer> insertHandler = new InsertExecuteHandler();
	static final ExecuteHandler<Integer> updateHandler = new UpdateExecuteHandler();
	static final ExecuteHandler<ResultSet> queryHandler = new QueryExecuteHandler();

	@Override
	public Object execute(StatementContext context, ExecuteCallback callback)
			throws SQLException {
		switch (context.getCurrentBatch().getAnalyzeResult().getStatementType()) {
		case SELECT:
			return this.doQuery(context, callback);
		case INSERT:
		case UPDATE:
		case DELETE:
			return doUpdate(context, callback);
		default:
			break;
		}
		return null;
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

	private Integer doUpdate(StatementContext context, ExecuteCallback callback)
			throws SQLException {
		Map<BatchItem, Set<RouteTarget>> batchItems = context.getExecuteInfosMap();
		StatementType statementType = context.getCurrentBatch().getAnalyzeResult().getStatementType();
		ExecuteHandler<Integer> hanlder = (ExecuteHandler<Integer>) getHandler(statementType);
		Set<RouteTarget> targets = batchItems.get(context.getCurrentBatch());
		return this.doUpdate(hanlder, targets, context, callback);

	}

	private Integer doUpdate(ExecuteHandler<Integer> hanlder,
			Set<RouteTarget> targets, StatementContext context,
			ExecuteCallback callback) throws SQLException {
		
		Integer sumResult = 0;
		List<FutureUpdateExecuteCallback> futureHolders = new ArrayList<FutureUpdateExecuteCallback>();
		int i = 0;
		int n = (targets.size() + 1);
		CyclicBarrier barrier = new CyclicBarrier(n);
		boolean asyn = (targets.size() > 1);
		boolean autoClosed=(targets.size() > 1);

		
		for (RouteTarget target : targets) {
			FutureUpdateExecuteCallback futureCallback = this.createUpdateCallback(target.getBatchItem().getMatchTable()
							, barrier, callback, asyn,autoClosed);
			
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
			} catch(BrokenBarrierException bbe){
				logger.error("not enough available thread to excute task! check your thread pool size!", bbe);
				throw new ShardException("not enough available thread to excute task!", bbe);
			} catch (Exception e) {
				logger.error("asyn execute error!targets=" + targets, e);
				throw new ShardException("asyn execute error!", e);
			}

		}
		return sumResult;
	}

	public ResultSet doQuery(StatementContext context, ExecuteCallback callback)
			throws SQLException {
		
		Map<BatchItem, Set<RouteTarget>> batchItems = context
				.getExecuteInfosMap();
		StatementType statementType = context.getCurrentBatch()
				.getAnalyzeResult().getStatementType();
		ExecuteHandler<ResultSet> hanlder = (ExecuteHandler<ResultSet>) getHandler(statementType);
		Set<RouteTarget> targets = batchItems.get(context.getCurrentBatch());
		return this.doQuery(hanlder, targets, context, callback);
	}

	private ResultSet doQuery(final ExecuteHandler<ResultSet> hanlder,
			final Set<RouteTarget> targets, final StatementContext context,
			final ExecuteCallback callback) throws SQLException {
		if(targets.isEmpty()){
			throw new ShardException("no target found!batch="+context.getCurrentBatch());
		}
		ResultSet[] results = new ResultSet[targets.size()];
		List<FutureQueryExecuteCallback> futureHolders = new ArrayList<FutureQueryExecuteCallback>();
		int i = 0;
		int n = (targets.size() + 1);
		CyclicBarrier barrier = new CyclicBarrier(n);
		boolean asyn = (targets.size() > 1); 
		for (RouteTarget target : targets) {
			FutureQueryExecuteCallback futureCallback = this.createCallback(target.getBatchItem().getMatchTable(), barrier, callback, asyn);
			futureHolders.add(futureCallback);
			//决定读写库
			results[i++] = hanlder.handle(target, context, futureCallback);
		}
		if (asyn){
			try {
				barrier.await();
				for (i = 0; i < results.length; i++) {
					results[i] = (ResultSet) futureHolders.get(i).future.get();
				}
			}catch(BrokenBarrierException bbe){
				throw new ShardException("not enough available thread to excute task!", bbe);
			}catch (Exception e) {
				logger.error("asyn execute error!targets=" + targets, e);
				throw new ShardException("asyn execute error!", e);
			}
		}
		
		//查询语句查询的数据库大于1个的，会走结果集合并逻辑。
		if (results.length > 1) {
			ResultSet mergeResult=MergerBuilder.buildResultSetMerger(context).merge(results, context);
			return mergeResult;
		} else {
			return results[0];
		}
	}

	
	FutureUpdateExecuteCallback createUpdateCallback(TableInfo tableInfo,
			CyclicBarrier barrier, final ExecuteCallback callback, boolean asyn,boolean autoClosed) {

		final ThreadPoolExecutor threadPool = Configurations.getInstance().getThreadPool(tableInfo.getName());
		
		FutureUpdateExecuteCallback resultCallback = new FutureUpdateExecuteCallback(
				threadPool, asyn, callback, barrier, ConnectionContext
						.getContext().getTransaction(),autoClosed);
		return resultCallback;
	}

	
	
	FutureQueryExecuteCallback createCallback(TableInfo tableInfo,
			CyclicBarrier barrier, final ExecuteCallback callback, boolean asyn) {

		final ThreadPoolExecutor threadPool = Configurations.getInstance()
				.getThreadPool(tableInfo.getName());
		
		FutureQueryExecuteCallback resultCallback = new FutureQueryExecuteCallback(
				threadPool, asyn, callback, barrier, ConnectionContext
						.getContext().getTransaction());
		return resultCallback;
	}
	

}
