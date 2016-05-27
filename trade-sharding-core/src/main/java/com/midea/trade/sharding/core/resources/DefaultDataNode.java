package com.midea.trade.sharding.core.resources;
import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midea.trade.sharding.core.alarm.Alarm;
import com.midea.trade.sharding.core.jdbc.ConnectionProvider;
import com.midea.trade.sharding.core.loadbalance.ha.DataNodeChecker;


/**
 * 默认 DataNode 实现
 */
public class DefaultDataNode implements DataNode {
	
	static final Logger logger=LoggerFactory.getLogger(DefaultDataNode.class);
	
	String id;
	
	ConnectionProvider connectionProvider;
	
	Alarm alarm;

	@Override
	public boolean isMarster() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSalve() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public DataNode[] getMarsters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DataNode[] getSlaves() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isAlive() {
		return DataNodeChecker.dataNodeAlive(this);
	}

	@Override
	public Connection getConnection() throws SQLException {
		try{
		return connectionProvider.getConnection();
		}catch(SQLException e){
			logger.error("fetch connection error!datanode id="+id,e);
			throw e;
		}
	}
	
	@Override
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setConnectionProvider(ConnectionProvider connectionProvider) {
		this.connectionProvider = connectionProvider;
	}

	@Override
	public Alarm getAlarm() {
		return alarm;
	}

	public void setAlarm(Alarm alarm) {
		this.alarm = alarm;
	}

}
