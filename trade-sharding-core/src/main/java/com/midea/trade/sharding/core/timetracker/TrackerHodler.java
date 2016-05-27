package com.midea.trade.sharding.core.timetracker;

/**
 * 耗时监控器的持有者
 */
public class TrackerHodler {
	
	long threshold;
	
	TrackPoint trackPoint;
	
	Tracker tracker;
	
	String tableName;

	public long getThreshold() {
		return threshold;
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

	public TrackPoint getTrackPoint() {
		return trackPoint;
	}

	public void setTrackPoint(TrackPoint trackPoint) {
		this.trackPoint = trackPoint;
	}

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public String toString() {
		return "TrackerHodler [threshold=" + threshold + ", trackPoint="
				+ trackPoint + ", tracker=" + tracker + ", tableName="
				+ tableName + "]";
	}

	public void regist() {
		if(tableName==null || tableName.length()==0){
			Tracker.addTracker(this);
		}else{
			Tracker.addTracker(tableName, this);
		}
	}


}
