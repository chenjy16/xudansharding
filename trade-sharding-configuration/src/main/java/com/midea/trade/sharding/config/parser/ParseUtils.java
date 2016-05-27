package com.midea.trade.sharding.config.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 配置解析内部工具
 */
public class ParseUtils {

	static final Map<String, Parser<?>> parsersMap = new ConcurrentHashMap<String, Parser<?>>();
	static {
		parsersMap.put(ConfigurationLoader.BEAN_TAG,new BeanConfigParser());
		parsersMap.put(ConfigurationLoader.DATA_NODE_TAG,new DataNodeConfigParser());
		parsersMap.put(ConfigurationLoader.NAME_NODE_TAG,new NameNodeConfigParser());
		parsersMap.put(ConfigurationLoader.TABLE_TAG, new TableConfigParser());
		parsersMap.put(ConfigurationLoader.THREAD_POOL_TAG,new ThreadPoolConfigParser());
		parsersMap.put(ConfigurationLoader.INCLUDE_TAG, new IncludeConfigParser());
		parsersMap.put(ConfigurationLoader.TRACKER_TAG, new TrackerConfigParser());
	}

	public static String getAttr(Element e, String name) {
		String value = e.getAttribute(name);
		return ((value == null || value.trim().length() <= 0) ? null : value
				.trim());
	}

	public static List<Node> getNodeList(NodeList nodeList, String tagName) {
		List<Node> results = new ArrayList<Node>();
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element
					&& node.getNodeName().equalsIgnoreCase(tagName)) {
				results.add(node);
			} else {
				continue;
			}

		}
		return results;
	}

	public static List<Node> getNodeList(NodeList nodeList) {
		List<Node> results = new ArrayList<Node>();
		int len = nodeList.getLength();
		for (int i = 0; i < len; i++) {
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				results.add(node);
			} else {
				continue;
			}

		}
		return results;
	}

	public static Parser<?> getParser(Node node) {
		return parsersMap.get(node.getNodeName());
	}
}
