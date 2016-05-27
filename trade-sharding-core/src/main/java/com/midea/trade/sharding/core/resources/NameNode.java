package com.midea.trade.sharding.core.resources;

import java.util.List;

/**
 * NameNode 约束，一个Namenode管理若干个Datanode
 */
public interface NameNode {
  

	public List<DataNodeHolder> getDataNodes();

	public List<DataNodeHolder> getWriteNodes();

	public List<DataNodeHolder> getReadNodes();
	/**
	 * 当检测到故障的时候从NameNode中摘除
	 * @param dataNode
	 */
	public DataNodeHolder remove(DataNodeHolder dataNode);
	
	public String getId();
	
	public LoadBanlance getLoadBanlance();
}
