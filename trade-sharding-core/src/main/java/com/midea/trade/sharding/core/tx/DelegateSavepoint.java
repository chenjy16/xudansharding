package com.midea.trade.sharding.core.tx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Savepoint
 */
public class DelegateSavepoint implements Savepoint {
	static final AtomicInteger savepointIdGen=new AtomicInteger(Integer.MIN_VALUE);
	Set<SavepointDesc> savePoints=new LinkedHashSet<SavepointDesc>(); 
	String name;
	int savepointId;
	
	DelegateSavepoint(String name){
		this.name=name;
	}
	@Override
	public int getSavepointId() throws SQLException {
		if(savepointId==0){
			savepointId=savepointIdGen.incrementAndGet();
		}
		return savepointId;
	}

	@Override
	public String getSavepointName() throws SQLException {
		return name;
	}
	public void addSavepoint(SavepointWrapper savepoint){
	SavepointDesc desc=new SavepointDesc();
	savePoints.add(desc);
	desc.connection=savepoint.getConnection();
	desc.savepoint=savepoint;
		
	}
	
	
	class SavepointDesc{
		Connection connection;
		Savepoint savepoint;
		
		public void rollback() throws SQLException{
			connection.rollback(savepoint);
		}
		public void releaseSavepoint() throws SQLException{
			connection.releaseSavepoint(savepoint);
		}
	}

}
