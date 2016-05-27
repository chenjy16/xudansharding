package com.midea.trade.sharding.config.parser;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.DataNodeConfig;

/**
 * DataNode配置解析器
 * 
 */
public class DataNodeConfigParser implements Parser<DataNodeConfig> {

	@Override
	public DataNodeConfig parse(Element el) {
		DataNodeConfig dataNode = new DataNodeConfig();
		String ref = ParseUtils.getAttr(el, "ref");
		dataNode.setRef(ref);

		String id = ParseUtils.getAttr(el, ID_ATTR);
		dataNode.setId(id);
		String slaves = ParseUtils.getAttr(el, "slaves");
		if (slaves != null) {
			dataNode.setSlaves(slaves.split("[,]"));
		}
		String parent = ParseUtils.getAttr(el, "parent");
		dataNode.setParent(parent);

		String accessMode = ParseUtils.getAttr(el, "access-mode");
		dataNode.setAccessMode(accessMode);
		String weight = ParseUtils.getAttr(el, "weight");
		if (weight != null) {
			dataNode.setWeight(Long.valueOf(weight));
		}

		List<Node> nodeList = ParseUtils.getNodeList(el.getChildNodes());
		for (Node node : nodeList) {
			dataNode.getProperties().put(
					node.getNodeName(),
					(node.getTextContent() == null ? null : node
							.getTextContent().trim()));
		}
		
		String alarmClass = ParseUtils.getAttr(el, "alarm");
		if (alarmClass != null) {
			dataNode.setAlarmClass(alarmClass);
		} else if(parent != null){
			dataNode.setAlarmClass(Configurations.getInstance()
					.getDataNodeConfig(parent).getAlarmClass());
		}

		Configurations.registerConfig(dataNode);
		return dataNode;
	}

}
