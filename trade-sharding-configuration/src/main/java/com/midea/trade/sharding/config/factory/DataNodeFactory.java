package com.midea.trade.sharding.config.factory;

import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.DataNodeConfig;
import com.midea.trade.sharding.core.alarm.Alarm;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.factory.ObjectFactory;
import com.midea.trade.sharding.core.jdbc.DataSourceConnectionProvider;
import com.midea.trade.sharding.core.resources.DefaultDataNode;



/**
 * DataNode配置工厂
 * 
 */
public class DataNodeFactory implements ObjectFactory<DataNodeConfig> {

	@Override
	public Object create(DataNodeConfig config) {
		DefaultDataNode dataNode = new DefaultDataNode();
		DataSourceConnectionProvider connectionProvider = new DataSourceConnectionProvider();
		connectionProvider.setDatasource(Configurations.getInstance().getDataSource(config.getId()));
		
		// TODO DATANODE init
		dataNode.setId(config.getId());
		dataNode.setConnectionProvider(connectionProvider);
		
		String alarmClass = config.getAlarmClass();
		if(alarmClass!=null){
			try {
				Alarm alarm = (Alarm) Class.forName(alarmClass).newInstance();
				dataNode.setAlarm(alarm);
			}  catch (Exception e) {
				throw new ConfigurationException(
						"datanode alarm init faile! class="
								+ alarmClass);
			}
		}
		return dataNode;
	}

}
