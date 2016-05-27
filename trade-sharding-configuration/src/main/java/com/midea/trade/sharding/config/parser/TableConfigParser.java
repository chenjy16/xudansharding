package com.midea.trade.sharding.config.parser;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.FunctionConfig;
import com.midea.trade.sharding.config.NameNodeConfig;
import com.midea.trade.sharding.config.NameNodeReferenceConfig;
import com.midea.trade.sharding.config.TableConfig;
import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.shard.Script;
import com.midea.trade.sharding.core.shard.ShardType;
import com.midea.trade.sharding.core.timetracker.TrackPoint;
import com.midea.trade.sharding.core.timetracker.Tracker;
import com.midea.trade.sharding.core.timetracker.TrackerHodler;

/**
 * Table配置解析器
 * 
 */
public class TableConfigParser implements Parser<TableConfig> {

	@Override
	public TableConfig parse(Element el) {
		TableConfig tableConfig = new TableConfig();
		String tableName = ParseUtils.getAttr(el, "name");
		tableConfig.setName(tableName);
		String threadPoolId = ParseUtils.getAttr(el, "threadpool");
		tableConfig.setThreadPoolId(threadPoolId);
		String differName = ParseUtils.getAttr(el, "differ-name");
		if (differName != null) {
			tableConfig.setDifferName(Boolean.valueOf(differName));
		}
		List<Node> nodeNamesList = ParseUtils.getNodeList(el.getChildNodes(),
				"namenodes");
		int i = 0;
		for (Node node : nodeNamesList) {
			List<NameNodeConfig> nodeConfigList = parseNodeNames(tableName,
					(Element) node);
			for (NameNodeConfig item : nodeConfigList) {
				NameNodeReferenceConfig reference = createReference(item);
				tableConfig.addNode(reference);
				reference.setIndex(++i);
			}
		}
		
		String shardType = ParseUtils.getAttr(el, "shard-type");
		if (shardType != null) {
			tableConfig.setShardType(ShardType.parse(shardType));
		} else {
			tableConfig.setShardType(ShardType.BY_DATABASE);
		}
		
		if(tableConfig.getShardType() == ShardType.NO_SHARD){
			tableConfig.setFunction(FunctionConfig.NO_SHARD_FUNCTION_CONFIG);
			tableConfig.setColumns(new String[] {});
		} else {
			List<Node> funtionList = ParseUtils.getNodeList(el.getChildNodes(), "function");
			FunctionConfig function = parseFunction(funtionList);
			tableConfig.setFunction(function);
			tableConfig.setColumns(parseColumns(el));
		}
		
		parseTableTrackers(el, tableName);
		Configurations.registerConfig(tableConfig);
		return tableConfig;
	}
	
	static String[] parseColumns(Element el) {
		Set<String> columnSet = new LinkedHashSet<String>();
		String columns = ParseUtils.getAttr(el, "columns");
		if (columns != null) {
			columnSet.addAll(Arrays.asList(columns.split("[,]")));
		}
		List<Node> columnsNode = ParseUtils.getNodeList(el.getChildNodes(),
				"columns");
		if (columnsNode.size() > 0) {
			columnSet.clear();// 清空attribute配置
			List<Node> columnNodeList = ParseUtils.getNodeList(
					columnsNode.get(0).getChildNodes(), "column");
			for (Node item : columnNodeList) {
				if (item.getTextContent() != null) {
					columnSet.add(ParseUtils.getAttr((Element) item, "name"));
				}
			}
		}
		return columnSet.toArray(new String[] {});
	}

	static NameNodeReferenceConfig createReference(NameNodeConfig nameNode) {
		NameNodeReferenceConfig reference = new NameNodeReferenceConfig();
		if (nameNode.getRef() == null) {
			reference.setRef(nameNode.getId());
		} else {
			reference.setRef(nameNode.getRef());
		}
		reference.setSchema(nameNode.getSchema());
		reference.setOrgTableName(nameNode.getOrgTableName());
		reference.setTableName(nameNode.getTableName());
		return reference;
	}

	FunctionConfig parseFunction(List<Node> funtionList) {
		if (funtionList.isEmpty()) {
			throw new IllegalArgumentException("funtion dose not config!");
		}
		Element node = (Element) funtionList.get(0);
		FunctionConfig config = new FunctionConfig();
		String ref = ParseUtils.getAttr(node, "ref");
		String execute = ParseUtils.getAttr(node, "execute");

		config.setRef(ref);
		config.setExecute(execute);

		String type = ParseUtils.getAttr(node, "type");
		if (!isBlank(type) || !isBlank(execute)) {
			String language = ParseUtils.getAttr(node, "language");
			Script script = new Script();
			script.setLanguage(language);
			script.setContent(node.getTextContent());
			script.setExecute(execute);
			config.setScript(script);
		}

		return config;
	}

	private boolean isBlank(String content) {
		return content == null || content.trim().length() <= 0;
	}

	List<NameNodeConfig> parseNodeNames(String tableName, Element node) {
		String range = ParseUtils.getAttr(node, "range");
		if (range == null || range.trim().length() <= 0) {
			return parseDefinedNodeNames(tableName, node);
		}

		return parseReferNodeNames(tableName, node);
	}

	List<NameNodeConfig> parseDefinedNodeNames(String tableName, Element node) {
		List<Node> nodeNameList = ParseUtils.getNodeList(node.getChildNodes(),
				"namenode");
		List<NameNodeConfig> results = new LinkedList<NameNodeConfig>();
		for (Node item : nodeNameList) {
			NameNodeConfig nodeName = (NameNodeConfig) ParseUtils.getParser(
					item).parse((Element) item);
			nodeName.setOrgTableName(tableName);
			results.add(nodeName);
		}
		return results;
	}

	List<NameNodeConfig> parseReferNodeNames(String tableName, Element node) {
		String range = ParseUtils.getAttr(node, "range");
		String ref = ParseUtils.getAttr(node, "ref");
		String prefix = ParseUtils.getAttr(node, "prefix");
		String schema = ParseUtils.getAttr(node, "schema");
		if (prefix == null) {
			prefix = tableName;
		}
		List<NameNodeConfig> results = new LinkedList<NameNodeConfig>();
		String ranges[] = range.split("[-]");
		int start = Integer.valueOf(ranges[0]), end = Integer
				.valueOf(ranges[1]);
		for (int i = start; i < end + 1; i++) {
			NameNodeConfig config = new NameNodeConfig();
			config.setSchema(schema);
			config.setRef(ref);
			config.setId(prefix + i);
			config.setTableName(prefix + i);
			config.setOrgTableName(tableName);
			results.add(config);
		}

		return results;
	}
	
	void parseTableTrackers(Element el, String tableName) {
		List<Node> trackersNode = ParseUtils.getNodeList(el.getChildNodes(),
				"trackers");
		if (trackersNode.size() > 0) {
			List<Node> columnNodeList = ParseUtils.getNodeList(
					trackersNode.get(0).getChildNodes(), "tracker");
			for (Node item : columnNodeList) {
				TrackerHodler trackerHodler = new TrackerHodler();
				
				trackerHodler.setTableName(tableName);
				
				String type = ParseUtils.getAttr((Element) item, "type");
				trackerHodler.setTrackPoint(TrackPoint.parse(type));
				
				String threshold = ParseUtils.getAttr((Element) item, "threshold");
				trackerHodler.setThreshold(Long.valueOf(threshold));
				
				try {
					String className = ParseUtils.getAttr((Element) item, "class");
					Tracker tracker = (Tracker) Class.forName(className).newInstance();
					trackerHodler.setTracker(tracker);
				} catch (Exception e){
					throw new ConfigurationException(e);
				}
				
				trackerHodler.regist();
			}
		}
	}

}
