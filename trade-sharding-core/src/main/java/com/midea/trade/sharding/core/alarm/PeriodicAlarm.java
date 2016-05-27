package com.midea.trade.sharding.core.alarm;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 周期性警报
 * <p>
 * 这是周期性警报的抽象实现，子类需要做两件事：
 * 1. 实现抽象方法{@link #getAlarmCycle()}，约定警报的时间间隔
 * 2. 实现抽象方法{@link #getAlarmUnit()}，指定警报的间隔时间单位
 * 3. 实现抽象方法{@link #excuteAlarm(AlarmType, String)}
 * 就会根据这个时间间隔进行警报调用 excuteAlarm
 * </p>
 */
public abstract class PeriodicAlarm implements Alarm{

	private Map<AlarmType, Long> lastAlarmTimeMapping = new HashMap<AlarmType, Long>();
	
	protected PeriodicAlarm() {
		for(AlarmType type : AlarmType.values()){
			lastAlarmTimeMapping.put(type, 0L);
		}
	}
	
	/**
	 * 警报时间间隔
	 * @return
	 */
	protected abstract long getAlarmCycle();
	
	/**
	 * 对应报警的时间单位
	 * @return
	 */
	protected abstract TimeUnit getAlarmUnit();
	
	/**
	 * 实现报警内容
	 * @param type
	 * @param dataNodeId
	 */
	protected abstract void excuteAlarm(AlarmType type, String dataNodeId);
	
	
	@Override
	public void excute(AlarmType type, String dataNodeId) {
		long now = System.currentTimeMillis();
		long space = TimeUnit.MILLISECONDS.convert(getAlarmCycle(), getAlarmUnit());
		long lastAlarmTime = this.lastAlarmTimeMapping.get(type);
		
		if(now - space > lastAlarmTime){
			excuteAlarm(type, dataNodeId);
			this.lastAlarmTimeMapping.put(type, now);
		}
	}

}

