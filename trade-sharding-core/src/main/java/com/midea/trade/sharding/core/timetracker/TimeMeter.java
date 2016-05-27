package com.midea.trade.sharding.core.timetracker;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Maps;

/**
 * 一个时间记录表，生命周期随ConnectionContext一致
 */
public class TimeMeter {
	
	static final ThreadLocal<TimeMeter> threadLocal = new ThreadLocal<TimeMeter>();
	final Map<TrackPoint, Long> startTimes = Maps.newLinkedHashMap();
	private static final TimeUnit DEFAULT_UNIT = TimeUnit.MILLISECONDS; 

	static TimeMeter getContext() {
		TimeMeter local = threadLocal.get();
		if(local == null){
			local = new TimeMeter();
			threadLocal.set(local);
		}
		
		return local;
	}
	
	static void release(){
		getContext().startTimes.clear();
		threadLocal.set(null);
	}
	
	/**
	 * 记录起始时间
	 * @param TrackPoint 计时监控点
	 */
	void begin(TrackPoint point) {
		startTimes.put(point, System.currentTimeMillis());
	}

	/**
	 * 获取对应标签从 begin(lable) 到现在的时间，用 TimeUnit 进行格式化
	 * @param lable 计时标签
	 * @param timeUnit 时间单位
	 * @return
	 */
	long getTimeSpan(TrackPoint point, TimeUnit timeUnit) {
		if(!startTimes.containsKey(point))
			return 0L;
		
		long mills = System.currentTimeMillis() - startTimes.get(point);
		startTimes.remove(point);
		
		return timeUnit.convert(mills, TimeUnit.MILLISECONDS);
	}
	
	/**
	 * 获取对应标签从 begin(lable) 到现在的毫秒数
	 * @param lable 计时标签
	 * @return
	 */
	long getTimeSpan(TrackPoint point) {
		return getTimeSpan(point, DEFAULT_UNIT);
	}
	
	String getTimeSpanString(TrackPoint point, TimeUnit timeUnit) {
		return getTimeSpan(point, DEFAULT_UNIT) + getUnitString(timeUnit);
	}
	
	String getTimeSpanString(TrackPoint point) {
		return getTimeSpan(point, DEFAULT_UNIT) + getUnitString(DEFAULT_UNIT);
	}
	
	private static String getUnitString(TimeUnit timeUnit) {
		switch (timeUnit) {
			case DAYS:
				return " d";
			case HOURS:
				return " h";
			case MICROSECONDS:
				return " micro";
			case MILLISECONDS:
				return " ms";
			case MINUTES:
				return " min";
			case NANOSECONDS:
				return " nano";
			case SECONDS:
				return " sec";
		}
		return "";
	}
	
	/**
	 * 通用监控点
	 */
	public static void main(String[] args) throws InterruptedException {
		TrackPoint pointHook = TrackPoint.CONNECTION_CONTEXT;
		TimeMeter.getContext().begin(pointHook);
		Thread.sleep(1000L);
		System.out.println(TimeMeter.getContext().getTimeSpan(pointHook, TimeUnit.MILLISECONDS));
	}
	
}
