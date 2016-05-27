package com.midea.trade.sharding.config.factory;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.tomcat.dbcp.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.DataNodeConfig;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.factory.ObjectFactory;
import com.midea.trade.sharding.core.util.BeanUtils;

/**
 * 数据源节点工厂
 */
public class DataSourceFactory implements ObjectFactory<DataNodeConfig> {
	static Logger logger = LoggerFactory.getLogger(DataSourceFactory.class);

	@Override
	public Object create(DataNodeConfig config) {
		if (config.getId() == null) {
			return null;
		}
		BasicDataSource dataSource = new BasicDataSource();
		//大于0 ，进行连接空闲时间判断，或为0，对空闲的连接不进行验证；默认30分钟 
		dataSource.setMinEvictableIdleTimeMillis(1000 * 60 * 30);
		//在每次空闲连接回收器线程(如果有)运行时检查的连接数量
		dataSource.setNumTestsPerEvictionRun(20);
		//标记是否删除泄露的连接,如果他们超过了removeAbandonedTimout的限制.
		//如果设置为true, 连接被认为是被泄露并且可以被删除,如果空闲时间超过removeAbandonedTimeout.
		//设置为true可以为写法糟糕的没有关闭连接的程序修复数据库连接.
		dataSource.setRemoveAbandoned(true);
		//泄露的连接可以被删除的超时值, 单位秒
		dataSource.setRemoveAbandonedTimeout(30);
		//失效检查线程运行时间间隔，如果小于等于0，不会启动检查线程，默认-1 
		dataSource.setTimeBetweenEvictionRunsMillis(1000 * 60 * 5);
		dataSource.setTestOnBorrow(true);
		dataSource.setTestOnReturn(false);
		dataSource.setTestWhileIdle(true);//打开检查,用异步线程evict进行检查 
		dataSource.setValidationQuery("SELECT 1");
		
		Map<String, String> properties=getProperties(config);
		if(logger.isDebugEnabled()){
			logger.debug("datanode id=["+config.getId()+"] properties="+properties);
		}
		for (Map.Entry<String, String> entry :properties .entrySet()) {
			try {
				BeanUtils.setProperty(dataSource,
						entry.getKey(), entry.getValue());
			} catch (Exception e) {
				logger.error("create DataSource error!", e);
			}

		}
		
		return dataSource;
	}

	private Map<String, String> getProperties(DataNodeConfig config) {
		Map<String, String> results = new LinkedHashMap<String, String>();

		if (config.getParent() != null
				&& config.getParent().trim().length() > 0) {
			DataNodeConfig parent = Configurations.getInstance()
					.getDataNodeConfig(config.getParent().trim());
			if (parent == null) {
				throw new ConfigurationException(
						"datanode config error!parent is not found!parent="
								+ config.getParent());
			}
			results.putAll(getProperties(parent));
		} else {
			return config.getProperties();
		}
		results.putAll(config.getProperties());
		return results;
	}

}
