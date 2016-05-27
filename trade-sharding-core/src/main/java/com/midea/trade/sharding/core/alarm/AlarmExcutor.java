package com.midea.trade.sharding.core.alarm;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import com.midea.trade.sharding.core.resources.DataNode;


/**
 * 警报执行器
 */
public final class AlarmExcutor {
	
	private static final Executor cacheExecutor = Executors.newCachedThreadPool();
	
	public static void doAlarm(final AlarmType alarmType, final DataNode dataNode){
		final Alarm alarm = dataNode.getAlarm();
		
		if(alarm!=null){
			cacheExecutor.execute(new Runnable() {
				@Override
				public void run() {
					alarm.excute(alarmType, dataNode.getId());
				}
			});
		}
	}

}

