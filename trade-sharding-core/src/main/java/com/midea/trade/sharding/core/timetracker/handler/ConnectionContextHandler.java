package com.midea.trade.sharding.core.timetracker.handler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerHodler;


/**
 * 耗时监控处理器：链接从逻辑建立到逻辑关闭的耗时
 * 
 */
public class ConnectionContextHandler extends TrackPointHandler{
	
	private static Logger logger = LoggerFactory.getLogger(ConnectionContextHandler.class);

	@Override
	protected void handle(TrackerHodler trackerHodler, String tableName, String sql, long costTime) {
		
		if(logger.isDebugEnabled()){
			logger.debug("PointHook [" + TrackPoint.CONNECTION_CONTEXT.name() + 
					"] ConnectionContext life cycle " + 
					costTime + " ms");
		}
		
		if(trackerHodler!=null && costTime>trackerHodler.getThreshold()) {
			TrackResult trackResult = new TrackResult();
			trackResult.setTableName(tableName);
			trackResult.setCostTime(costTime);
			
			super.asyncDoTrack(trackerHodler.getTracker(), trackResult);
		}
	}

}
