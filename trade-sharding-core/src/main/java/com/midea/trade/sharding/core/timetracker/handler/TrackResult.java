package com.midea.trade.sharding.core.timetracker.handler;

/**
 * 耗时监控结果
 */
public class TrackResult {
	
	private String tableName;
	
	private String sql;
	
	private long costTime;
	
	TrackResult() {}

	public String getTableName() {
		return tableName;
	}

	void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSql() {
		return sql;
	}

	void setSql(String sql) {
		this.sql = sql;
	}

	public long getCostTime() {
		return costTime;
	}

	void setCostTime(long costTime) {
		this.costTime = costTime;
	}

	@Override
	public String toString() {
		return "TrackResult [tableName=" + tableName + ", sql=" + sql
				+ ", costTime=" + costTime + "]";
	}

}
