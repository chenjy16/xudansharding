package com.midea.trade.sharding.core.jdbc.aggregate;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.midea.trade.sharding.core.jdbc.result.RowSet;

 

/**
 * 最大值
 */
@SuppressWarnings("rawtypes")
public class Max implements Aggregate<Comparable> {
	private String name;
	private String key;
	private Comparable value;
	private final int resultIndex;

	public Max(String name, String key, int resultIndex) {
		this.name = name;
		this.key = key;
		this.resultIndex=resultIndex;
	}

	@Override
	public String key() {
		return key;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addRow(ResultSet resultSet,RowSet row) throws SQLException {
		Comparable item=(Comparable) row.getObject(resultIndex);
		if(item==null){
			return;
		}
		if (value == null) {
			value = item;
		}
		value = (value.compareTo(item) > 0 ? value : item);
	}

	@Override
	public Comparable value() {
		return value;
	}

	@Override
	public String getColumnName() {
		return name;
	}

	@Override
	public int resultIndex() { 
		return resultIndex;
	}
}
