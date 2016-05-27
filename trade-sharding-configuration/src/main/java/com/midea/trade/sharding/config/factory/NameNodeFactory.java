package com.midea.trade.sharding.config.factory;

import java.util.Iterator;

import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.DataNodeReferenceConfig;
import com.midea.trade.sharding.config.NameNodeConfig;
import com.midea.trade.sharding.core.factory.ObjectFactory;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.DataNodeHolder;
import com.midea.trade.sharding.core.resources.DefaultNameNode;
import com.midea.trade.sharding.core.resources.LoadBanlance;

/**
 * NameNode配置工厂
 * 
 */
public class NameNodeFactory implements ObjectFactory<NameNodeConfig> {

	@Override
	public Object create(NameNodeConfig config) {
		DefaultNameNode nameNode = new DefaultNameNode();
		Iterator<DataNodeReferenceConfig> iterator = config.getReferenceNodes()
				.iterator();
		while (iterator.hasNext()) {
			DataNodeReferenceConfig dataNodeConfig = iterator.next();
			DataNodeHolder holder = this.createDataNode(dataNodeConfig);
			nameNode.addDataNode(holder);

		}
		nameNode.setId(config.getId());
		nameNode.setLoadBanlance(LoadBanlance.parse(config.getLoadbalance()));
		return nameNode;
	}

	private DataNodeHolder createDataNode(DataNodeReferenceConfig config) {
		DataNode dataNode = null;
		DataNodeReferenceConfig nodeConfig = config;
		if (nodeConfig.getRef() != null) {
			dataNode = Configurations.getInstance().getDataNode(
					nodeConfig.getRef());
		}
		if (dataNode == null) {
			throw new IllegalArgumentException("datanode must not null!id="
					+ nodeConfig.getRef());
		}
		DataNodeHolder holder = new DataNodeHolder(dataNode);

		holder.setCanRead(nodeConfig.getAccessMode().isCanRead());
		holder.setCanWrite(nodeConfig.getAccessMode().isCanWrite());
		holder.setWeight(nodeConfig.getWeight());
		holder.setId(nodeConfig.getRef());
		return holder;
	}

}
