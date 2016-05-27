package com.midea.trade.sharding.jdbc.rw;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.resources.NameNode;
import com.midea.trade.sharding.jdbc.ProviderDesc;



/**
 * 读写分离场景中必须指定使用哪一个NameNode
 * 
 */
public class NameNodeResolver {

	public static NameNode resolve(ProviderDesc desc) throws ConfigurationException {
		NameNode nameNode = Configurations.getInstance().getNameNodeById(desc.getNameNodeId());
		
		if(nameNode == null){
			throw new ConfigurationException("NameNode id ["+desc.getNameNodeId()+"] does not exsit!");
		}
		return nameNode;
	}

}
