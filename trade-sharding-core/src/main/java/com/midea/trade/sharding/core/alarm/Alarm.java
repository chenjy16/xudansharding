package com.midea.trade.sharding.core.alarm;

/**
 * 警报接口约束，提供相应警报的对接功能
 * <p>
 * 接入方通过配置指定警报类型对应的实现类
 * 当触发一个警报时，由警报执行器找到对应的警报实现类进行接口调用
 * </p>
 */
public interface Alarm {
	
	void excute(AlarmType type, String dataNodeId);

}

