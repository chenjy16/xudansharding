package com.midea.trade.sharding.core.resources;

import java.util.ArrayList;
import java.util.List;

/**
 * 默认 NameNode 实现
 */
public class DefaultNameNode implements NameNode {

	List<DataNodeHolder> dataNodeList = new ArrayList<DataNodeHolder>();

	List<DataNodeHolder> writeDataNodes = new ArrayList<DataNodeHolder>();

	List<DataNodeHolder> readDataNodes = new ArrayList<DataNodeHolder>();

	private String id;

	private LoadBanlance loadBanlance;

	public void addDataNode(DataNodeHolder holder) {
		dataNodeList.add(holder);

		if (holder.canRead())
			readDataNodes.add(holder);

		if (holder.canWrite())
			writeDataNodes.add(holder);

	}

	@Override
	public List<DataNodeHolder> getDataNodes() {
		return dataNodeList;
	}

	@Override
	public List<DataNodeHolder> getWriteNodes() {
		return writeDataNodes;
	}

	@Override
	public List<DataNodeHolder> getReadNodes() {
		return readDataNodes;
	}

	@Override
	public DataNodeHolder remove(DataNodeHolder dataNode) {
		readDataNodes.remove(dataNode);
		writeDataNodes.remove(dataNode);
		dataNodeList.remove(dataNode);

		return dataNode;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LoadBanlance getLoadBanlance() {
		return this.loadBanlance;
	}

	public void setLoadBanlance(LoadBanlance loadBanlance) {
		this.loadBanlance = loadBanlance;
	}

}
