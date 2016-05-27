package com.midea.trade.sharding.config;

/**
 * Tracker配置实体
 */
public class TrackerConfig implements Config {
	
	private static final long serialVersionUID = 1L;

	String type;
	
	long threshold;
	
	String className;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getThreshold() {
		return threshold;
	}

	public void setThreshold(long threshold) {
		this.threshold = threshold;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "TrackerConfig [type=" + type + ", threshold=" + threshold
				+ ", className=" + className + "]";
	}

}
