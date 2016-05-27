package com.midea.trade.sharding.core.resources;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.midea.trade.sharding.core.alarm.Alarm;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.TrackerExecutor;



/**
 * DataNode 实例持有者
 */
public class DataNodeHolder implements DataNode {

    String id;

	boolean canRead;
	
	boolean canWrite;
	
	Long weight;

    final DataNode dataNode;
    
    DataNode[] slaves = new DataNode[0];

	public DataNodeHolder(DataNode dataNode) {
		
		this.dataNode = dataNode;
	}

	@Override
	public boolean isMarster() {
		return dataNode.isMarster();
	}

	@Override
	public boolean isSalve() {
		return dataNode.isSalve();
	}

	@Override
	public DataNode[] getMarsters() {
		return dataNode.getMarsters();
	}

	@Override
	public DataNode[] getSlaves() {
		return slaves;
	}

	@Override
	public Connection getConnection() throws SQLException {
		try {
			TrackerExecutor.trackBegin(TrackPoint.GET_CONNECTION);
			return dataNode.getConnection();
		} finally {
			TrackerExecutor.trackEnd(TrackPoint.GET_CONNECTION);
		}
	}
	
	@Override
	public boolean isAlive() {
		return dataNode.isAlive();
	}

	public boolean canRead() {
		return canRead;
	}

	public void setCanRead(boolean canRead) {
		this.canRead = canRead;
	}

	public boolean canWrite() {
		return canWrite;
	}

	public void setCanWrite(boolean canWrite) {
		this.canWrite = canWrite;
	}

	public DataNode getDataNode() {
		return dataNode;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public void setSlaves(List<DataNode> slaves) {
		this.slaves = slaves.toArray(new DataNode[slaves.size()]);
	}

    @Override
    public String toString() {
        return "DataNodeHolder{" +
                "weight=" + weight +
                ", id='" + id + '\'' +
                ", canRead=" + canRead +
                ", canWrite=" + canWrite +
                '}';
    }

	@Override
	public Alarm getAlarm() {
		return dataNode.getAlarm();
	}


}
