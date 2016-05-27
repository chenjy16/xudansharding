package com.midea.trade.sharding.druid.route;

import java.util.LinkedHashMap;
import java.util.Map;


public class SQLMerge {
	
	private LinkedHashMap<String, Integer> orderByCols;
	private HavingCols havingCols;
	private Map<String, Integer> mergeCols;
	private String[] groupByCols;
	private boolean hasAggrColumn;

	public LinkedHashMap<String, Integer> getOrderByCols() {
		return orderByCols;
	}

	public void setOrderByCols(LinkedHashMap<String, Integer> orderByCols) {
		this.orderByCols = orderByCols;
	}

	public Map<String, Integer> getMergeCols() {
		return mergeCols;
	}

	public void setMergeCols(Map<String, Integer> mergeCols) {
		this.mergeCols = mergeCols;
	}

	public String[] getGroupByCols() {
		return groupByCols;
	}

	public void setGroupByCols(String[] groupByCols) {
		this.groupByCols = groupByCols;
	}

	public boolean isHasAggrColumn() {
		return hasAggrColumn;
	}

	public void setHasAggrColumn(boolean hasAggrColumn) {
		this.hasAggrColumn = hasAggrColumn;
	}

	public HavingCols getHavingCols() {
		return havingCols;
	}

	public void setHavingCols(HavingCols havingCols) {
		this.havingCols = havingCols;
	}

}
