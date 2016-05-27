package com.midea.trade.sharding.core.timetracker.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerHodler;


/**
 * 耗时监控处理器：从链接池中获取链接的耗时
 */
public class GetConnectionHandler extends TrackPointHandler{

	private static Logger logger = LoggerFactory.getLogger(GetConnectionHandler.class);
	
	@Override
	protected void handle(TrackerHodler trackerHodler, String tableName, String sql, long costTime) {
		
		if(logger.isDebugEnabled()){
			logger.debug("PointHook ["+TrackPoint.GET_CONNECTION.name()+"] Thread [" + Thread.currentThread().getName() +
					"] get connection from table [" + tableName +
					"] cost " + costTime + " ms");
		}
		
		if(trackerHodler!=null && costTime>trackerHodler.getThreshold()) {
			TrackResult trackResult = new TrackResult();
			trackResult.setTableName(tableName);
			trackResult.setCostTime(costTime);
			
			super.asyncDoTrack(trackerHodler.getTracker(), trackResult);
		}
	}

}
