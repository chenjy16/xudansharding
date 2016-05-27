package com.midea.trade.sharding.core.timetracker.handler;
import java.util.Map;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.google.common.collect.Maps;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.Tracker;
import com.midea.trade.sharding.core.timetracker.TrackerHodler;

/**
 * 耗时监控处理器基类
 */
public abstract class TrackPointHandler {
	
	private static final Executor cacheExecutor = Executors.newCachedThreadPool();
	
	private static Map<TrackPoint, TrackPointHandler> handlers = Maps.newHashMap();
	
	static {
		handlers.put(TrackPoint.GET_CONNECTION,new GetConnectionHandler());
		handlers.put(TrackPoint.CONNECTION_CONTEXT,new ConnectionContextHandler());
		handlers.put(TrackPoint.EXECUTE_SQL,new ExecuteSqlHandler());
		handlers.put(TrackPoint.PARSE_SQL, new ParseSqlHandler());
	}
	
	public static void handle(TrackPoint trackPoint, TrackerHodler trackerHodler, 
			String tableName, String sql, long costTime){
		if(!handlers.containsKey(trackPoint)){
			return;
		}
		handlers.get(trackPoint).handle(trackerHodler, tableName, sql, costTime);
	}
	
	protected abstract void handle(TrackerHodler trackerHodler, String tableName, 
			String sql, long costTime);

	
	
	protected void asyncDoTrack(final Tracker tracker, final TrackResult trackResult){
		cacheExecutor.execute(new Runnable() {
			@Override
			public void run() {
				tracker.doTrack(trackResult);
			}
		});
	}
	
}
