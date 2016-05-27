package com.midea.trade.sharding.core.timetracker.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerHodler;

/**
 * 耗时监控处理器：sql的执行耗时
 */
public class ExecuteSqlHandler extends TrackPointHandler{
	private static Logger logger = LoggerFactory.getLogger(ExecuteSqlHandler.class);
	
	@Override
	protected void handle(TrackerHodler trackerHodler, String tableName, String sql, long costTime) {
		
		if(logger.isDebugEnabled()){
			logger.debug("PointHook [" + TrackPoint.EXECUTE_SQL.name() + 
					"] BaseExecuteHandler execute sql [" + sql + 
					"] cost " + costTime + " ms");
		}
		if(trackerHodler!=null && costTime>trackerHodler.getThreshold()) {
			TrackResult trackResult = new TrackResult();
			trackResult.setTableName(tableName);
			trackResult.setCostTime(costTime);
			trackResult.setSql(sql);
			super.asyncDoTrack(trackerHodler.getTracker(), trackResult);
		}
	}

}
