package com.midea.trade.sharding.core.resources;
import java.sql.Connection;
import java.sql.SQLException;
import com.midea.trade.sharding.core.alarm.Alarm;


/**
 * DataNode 约束
 */
public interface DataNode {
	
	boolean isMarster();
	
	boolean isSalve();
	
	DataNode[] getMarsters();
	
	DataNode[] getSlaves();
	
	Connection getConnection() throws SQLException;
	
	String getId();
	
	boolean isAlive();
	
	Alarm getAlarm();

}
