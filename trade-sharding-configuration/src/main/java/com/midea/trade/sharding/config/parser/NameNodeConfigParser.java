package com.midea.trade.sharding.config.parser;

import java.util.List;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.midea.trade.sharding.config.AccessMode;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.DataNodeConfig;
import com.midea.trade.sharding.config.DataNodeReferenceConfig;
import com.midea.trade.sharding.config.NameNodeConfig;

/**
 * NameNode配置解析器
 * 
 */
public class NameNodeConfigParser implements Parser<NameNodeConfig> {
	String DATANODE_TAG = "datanode";
	String DATANODES_TAG = "datanodes";

	@Override
	public NameNodeConfig parse(Element el) {
		NameNodeConfig nameNode = new NameNodeConfig();
		String id = ParseUtils.getAttr(el, ID_ATTR);
		String loadbalance = ParseUtils.getAttr(el, LOADBLANCE_ATTR);
		String ref = ParseUtils.getAttr(el, "ref");
		String tableName = ParseUtils.getAttr(el, "tablename");
		String schema = ParseUtils.getAttr(el, "schema");
		nameNode.setTableName(tableName);
		nameNode.setRef(ref);
		nameNode.setId(id);
		nameNode.setSchema(schema);

		nameNode.setLoadbalance(loadbalance);
		List<Node> dataNodes = ParseUtils.getNodeList(el.getChildNodes(),
				DATANODES_TAG);
		if (dataNodes.size() > 0) {
			List<Node> nodes = ParseUtils.getNodeList(dataNodes.get(0)
					.getChildNodes(), DATANODE_TAG);
			for (Node node : nodes) {
				DataNodeConfig dataNodeConfig = (DataNodeConfig) ParseUtils
						.getParser(node).parse((Element) node);
				DataNodeReferenceConfig reference = createReference(dataNodeConfig);
				nameNode.addReferenceNode(reference);
			}
		}
		Configurations.registerConfig(nameNode);
		return nameNode;
	}

	DataNodeReferenceConfig createReference(DataNodeConfig dataNode) {
		DataNodeReferenceConfig reference = new DataNodeReferenceConfig();
		if (dataNode.getRef() != null) {
			reference.setRef(dataNode.getRef());
		} else {
			reference.setRef(dataNode.getId());
		}
		reference.setAccessMode(AccessMode.parse(dataNode.getAccessMode()));
		reference.setWeight(dataNode.getWeight());
		return reference;
	}

}
