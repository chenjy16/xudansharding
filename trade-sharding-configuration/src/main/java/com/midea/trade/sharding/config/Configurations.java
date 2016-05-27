package com.midea.trade.sharding.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.midea.trade.sharding.config.factory.DataNodeFactory;
import com.midea.trade.sharding.config.factory.DataSourceFactory;
import com.midea.trade.sharding.config.factory.NameNodeFactory;
import com.midea.trade.sharding.config.factory.TableDescriptionFactory;
import com.midea.trade.sharding.config.factory.ThreadPoolFactory;
import com.midea.trade.sharding.config.parser.ConfigurationLoader;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.factory.ObjectFactory;
import com.midea.trade.sharding.core.loadbalance.ha.DataNodeChecker;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.DataNodeHolder;
import com.midea.trade.sharding.core.resources.NameNode;
import com.midea.trade.sharding.core.resources.NameNodeHolder;
import com.midea.trade.sharding.core.resources.TableDescription;
import com.midea.trade.sharding.core.shard.Function;
import com.midea.trade.sharding.core.shard.ShardQueryGenerator;
import com.midea.trade.sharding.core.shard.ShardType;
import com.midea.trade.sharding.core.shard.TableInfo;

/**
 * 配置构建中心
 */
public class Configurations {
	
	static Logger logger = LoggerFactory.getLogger(Configurations.class);
	static Configurations instance = null;
	static final Map<String, NameNode> nameNodesMap = new HashMap<String, NameNode>();
	static final Map<String, List<NameNodeHolder>> tableToNameNodesMap = new HashMap<String, List<NameNodeHolder>>();
	static final Map<String, DataNode> dataNodesMap = new HashMap<String, DataNode>();
	static final Map<String, DataSource> dataSourceMap = new HashMap<String, DataSource>();
	static final Map<String, Function> functionsMap = new HashMap<String, Function>();
	static final Map<String, TableConfig> tableConfigMap = new HashMap<String, TableConfig>();
	static final Map<String, BeanConfig> beanConfigMap = new HashMap<String, BeanConfig>();
	static final Map<String, DataNodeConfig> dataNodesConfigMap = new HashMap<String, DataNodeConfig>();
	static final Map<String, NameNodeConfig> nameNodesConfigMap = new HashMap<String, NameNodeConfig>();
	static final Map<String, ThreadPoolConfig> threadPoolsConfigMap = new HashMap<String, ThreadPoolConfig>();
	static final Map<String, List<DataNode>> dataNodeMsMap = Maps.newHashMap();

	static final Map<String, ThreadPoolExecutor> threadPoolsHoldersMap = new HashMap<String, ThreadPoolExecutor>();
	static final Map<String, TableDescription> tableDescriptionsMap = new HashMap<String, TableDescription>();
	static final Map<String, Object> beansMap = new HashMap<String, Object>();
	static final ConfigurationLoader configurationLoader = new ConfigurationLoader();
	static final ObjectFactory<DataNodeConfig> dataNodeFactory = new DataNodeFactory();
	static final ObjectFactory<DataNodeConfig> dataSourceFactory = new DataSourceFactory();
	static final ObjectFactory<ThreadPoolConfig> threadPoolFactory = new ThreadPoolFactory();

	static final ObjectFactory<NameNodeConfig> nameNodeFactory = new NameNodeFactory();
	static final ObjectFactory<TableConfig> tableDescFactory = new TableDescriptionFactory();
	String DEFAULT_SQL_GENERATOR_CLASS = "com.midea.trade.sharding.jdbc.unparse.DefaultShardQueryGenerator";
	String LIMIT_AVG_SQL_GENERATOR_CLASS = "com.midea.trade.sharding.jdbc.unparse.LimitAggregateQueryGenerator";
	
	static final AtomicBoolean inited = new AtomicBoolean(false);
	
	/**
	 * 生成最终的sql
	 */
	ShardQueryGenerator generator;
	ShardQueryGenerator limitAvgGenerator;
	static ThreadPoolExecutor defaultExecuteThreadPool;

	private Configurations() {
	}

	public static Configurations getInstance() {
		if (instance == null) {
			instance = new Configurations();
		}
		return instance;
	}

	public void init(String configFilePath) throws ConfigurationException{
		if(!inited.compareAndSet(false, true)){
			throw new ConfigurationException("ShardingClient has been inited !");
		}
		try {
			configurationLoader.load(configFilePath);
		} catch (Exception e) {
			logger.error("load config error!", e);
		}
		initBeans();
		initDataSource();
		initDataNodes();
		initNameNodes();
		initTableDescription();
		indexNameNodes();
		initDataNodeMsMapping();
		initThreadPools();
		//关闭检测数据库是否可用的任务:同时不支持 ha方式 负载均衡
		//DataNodeChecker.startChecker("SELECT 1", dataNodesMap);
	}

	private void initThreadPools() {
		ThreadPoolConfig defaultConfig = new ThreadPoolConfig();
		defaultConfig.setId("ShardingClient-thread-default");
		defaultExecuteThreadPool = (ThreadPoolExecutor) threadPoolFactory.create(defaultConfig);
		
		for (ThreadPoolConfig config : threadPoolsConfigMap.values()) {
			ThreadPoolExecutor threadPool = (ThreadPoolExecutor) threadPoolFactory.create(config);
			if (threadPoolsHoldersMap.containsKey(config.getId())) {
				throw new ConfigurationException("duplicate configurations,threadpool config=" + config);
			}
			threadPoolsHoldersMap.put(config.getId(), threadPool);
		}
	}

	private void initBeans() {
		for (BeanConfig config : beanConfigMap.values()) {
			Object bean = createBean(config.className);
			beansMap.put(config.getId(), bean);
		}
		this.generator = (ShardQueryGenerator) createBean(DEFAULT_SQL_GENERATOR_CLASS);
		this.limitAvgGenerator = (ShardQueryGenerator) createBean(LIMIT_AVG_SQL_GENERATOR_CLASS);
	}

	private Object createBean(String className) {
		Object bean = null;
		try {
			bean = Class.forName(className).newInstance();
		} catch (Exception e) {
			logger.error("create bean error!class=" + className, e);
		}
		return bean;
	}

	private void initDataSource() {
		for (DataNodeConfig config : dataNodesConfigMap.values()) {
			DataSource dataSource = (DataSource) dataSourceFactory
					.create(config);
			if (dataSource != null) {
				dataSourceMap.put(config.getId(), dataSource);
			}
		}

	}

	private void initDataNodes() {
		for (DataNodeConfig config : dataNodesConfigMap.values()) {
			DataNode dataNode = (DataNode) dataNodeFactory.create(config);
			if (dataNodesMap.containsKey(dataNode.getId())) {
				throw new ConfigurationException(
						"duplicate configurations,dataNode config=" + config);
			}
			dataNodesMap.put(dataNode.getId(), dataNode);
		}

	}

	private void initNameNodes() {
		for (NameNodeConfig config : nameNodesConfigMap.values()) {
			NameNode nameNode = (NameNode) nameNodeFactory.create(config);
			if (nameNodesMap.containsKey(nameNode.getId())) {
				throw new ConfigurationException(
						"duplicate configurations,nameNode config=" + config);
			}
			nameNodesMap.put(nameNode.getId(), nameNode);
		}
	}

	private void initDataNodeMsMapping() {
		for (NameNode nameNode : nameNodesMap.values()) {
			for(DataNodeHolder dataNode : nameNode.getDataNodes()){
				DataNodeConfig config = dataNodesConfigMap.get(dataNode.getId());
				
				String[] slaveIds = config.getSlaves();
				if(slaveIds==null || slaveIds.length==0 || dataNodeMsMap.containsKey(config.getId()))
					continue;
				
				if(!dataNodesMap.containsKey(config.getId())) {
					throw new ConfigurationException(
							"missing configurations,dataNode config=" + config.getId());
				}
				
				List<DataNode> slaveList = Lists.newArrayList();
				for(String slaveId : slaveIds) {
					slaveList.add(dataNodesMap.get(slaveId));
				}
				
				dataNodeMsMap.put(config.getId(), slaveList);
				dataNode.setSlaves(slaveList);
			}
		}
	}

	private void initTableDescription() {
		for (TableConfig config : tableConfigMap.values()) {
			TableDescription desc = (TableDescription) tableDescFactory
					.create(config);
			String key = desc.getTableName().toUpperCase();
			if (tableDescriptionsMap.containsKey(key)) {
				throw new ConfigurationException(
						"duplicate configurations,table config=" + config);
			}
			tableDescriptionsMap.put(key, desc);
		}

	}

	private void indexNameNodes() {
		Set<Map.Entry<String, TableDescription>> entrySet = tableDescriptionsMap
				.entrySet();
		for (Map.Entry<String, TableDescription> entry : entrySet) {
			TableDescription desc = entry.getValue();
			for (NameNodeHolder holder : desc.getNameNodes()) {
				String key = (holder.getTableName()).toUpperCase();
				List<NameNodeHolder> nameNodes = tableToNameNodesMap.get(key);
				if (nameNodes == null) {
					nameNodes = new ArrayList<NameNodeHolder>();
					tableToNameNodesMap.put(key, nameNodes);
				}
				nameNodes.add(holder);
			}
		}
	}

	public static void registerConfig(Config config) {
		if (config instanceof TableConfig) {
			TableConfig table = (TableConfig) config;
			String key = table.getName().toUpperCase();
			if (tableConfigMap.containsKey(key)) {
				throw new ConfigurationException("tableconfig tablename=["
						+ table.getName()
						+ "] duplicate!please check your config!");
			}
			tableConfigMap.put(key, table);
		}
		if (config instanceof BeanConfig) {
			BeanConfig idConfig = (BeanConfig) config;
			String key = idConfig.getId();
			if (beanConfigMap.containsKey(key)) {
				throw new ConfigurationException("bean id=[" + key
						+ "] duplicate!please check your config!");
			}
			beanConfigMap.put(idConfig.getId(), idConfig);
		}
		if (config instanceof DataNodeConfig) {

			DataNodeConfig idConfig = (DataNodeConfig) config;
			if (idConfig.getId() == null) {
				return;
			}
			String key = idConfig.getId();
			if (dataNodesConfigMap.containsKey(key)) {
				throw new ConfigurationException("DataNode id=[" + key
						+ "] duplicate!please check your config!");
			}
			dataNodesConfigMap.put(idConfig.getId(), idConfig);
		}
		if (config instanceof NameNodeConfig) {
			NameNodeConfig idConfig = (NameNodeConfig) config;
			if (idConfig.getId() == null) {
				return;
			}
			String key = idConfig.getId();
			if (nameNodesConfigMap.containsKey(key)) {
				throw new ConfigurationException("NameNode id=[" + key
						+ "] duplicate!please check your config!");
			}
			nameNodesConfigMap.put(idConfig.getId(), idConfig);
		}
		if (config instanceof ThreadPoolConfig) {
			
			ThreadPoolConfig threadPoolConfig = (ThreadPoolConfig) config;
			if (threadPoolConfig.getId() == null){
				return;
			}
			String key = threadPoolConfig.getId();
			if (threadPoolsConfigMap.containsKey(key)) {
				throw new ConfigurationException("ThreadPool id=[" + key+ "] duplicate!please check your config!");
			}
			threadPoolsConfigMap.put(threadPoolConfig.getId(), threadPoolConfig);
		}

	}

	public NameNodeConfig getNameNodeConfig(String id) {
		return nameNodesConfigMap.get(id);
	}

	public DataNode getDataNode(String id) {
		return dataNodesMap.get(id);
	}
	
	public Map<String, DataNode> getDataNodeMap() {
		return dataNodesMap;
	}

	public DataSource getDataSource(String id) {
		return dataSourceMap.get(id);
	}

	public DataNodeConfig getDataNodeConfig(String id) {
		return dataNodesConfigMap.get(id);
	}

	public BeanConfig getBeanConfig(String id) {
		return beanConfigMap.get(id);
	}

	public Object getBean(String id) {
		return beansMap.get(id);
	}

	public TableConfig getTableConfig(String tableName) {
		return tableConfigMap.get(tableName.toUpperCase());
	}

	public Function getFunctionById(String id) {
		return functionsMap.get(id);
	}

	public ShardQueryGenerator getGenerator() {
		return generator;
	}

	public void setGenerator(ShardQueryGenerator generator) {
		this.generator = generator;
	}

	public void setLimitAvgGenerator(ShardQueryGenerator limitAvgGenerator) {
		this.limitAvgGenerator = limitAvgGenerator;
	}

	public ShardQueryGenerator getLimitAvgGenerator() {
		return limitAvgGenerator;
	}

	public ShardType getShardType(String table) {
		TableConfig tableConfig = this.getTableConfig(table);
		if (tableConfig == null) {
			return ShardType.NO_SHARD;
		}
		return tableConfig.getShardType();
	}

	public TableDescription getTableDescription(String tableName) {
		return tableDescriptionsMap.get(tableName.toUpperCase());
	}

	public NameNode getNameNodeById(String id) {
		return nameNodesMap.get(id);
	}

	public NameNodeHolder findNameNode(TableInfo tableInfo) {
		if (tableInfo.getOrgName() == null
				|| tableInfo.getOrgName().trim().length() <= 0) {
			throw new IllegalArgumentException(
					"tableName must not null or empty!");
		}
		String key = (tableInfo.getOrgName()).toUpperCase();

		List<NameNodeHolder> nodes = tableToNameNodesMap.get(key);
		if (nodes == null) {
			return null;
		}
		for (NameNodeHolder holder : nodes) {
			if (tableInfo.getSchema() != null
					&& tableInfo.getSchema().equalsIgnoreCase(
							holder.getSchema())) {
				return holder;
			}
		}
		return nodes.get(0);
	}

	public List<NameNodeHolder> getNameNodes(String orgTableName) {
		TableDescription desc = getTableDescription(orgTableName.toUpperCase());
		if (desc == null) {
			return null;
		}
		return desc.getNameNodes();
	}

	public NameNode getNameNode(String orgTableName, int i) {
		List<NameNodeHolder> nameNodes = getNameNodes(orgTableName
				.toUpperCase());
		return nameNodes.get(i);
	}

	public ThreadPoolExecutor getThreadPool() {
		return defaultExecuteThreadPool;
	}
	
	public ThreadPoolExecutor getThreadPool(String tableName) {
		tableName = tableName.toUpperCase();
		if(tableConfigMap.containsKey(tableName)){
			String threadPoolId = tableConfigMap.get(tableName).getThreadPoolId();
			if(threadPoolsHoldersMap.containsKey(threadPoolId)){
				return threadPoolsHoldersMap.get(threadPoolId);
			}
		}
		return defaultExecuteThreadPool;
	}

}
