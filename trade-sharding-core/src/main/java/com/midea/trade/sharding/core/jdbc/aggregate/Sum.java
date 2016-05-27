package com.midea.trade.sharding.core.jdbc.aggregate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.midea.trade.sharding.core.jdbc.result.RowSet;

/**
 * 求和
 */
public class Sum implements Aggregate<Number> {
	private String name;
	private String key;
	private Number value;
	private Map<ResultSet, Number> values = new HashMap<ResultSet, Number>();
	private final int resultIndex;

	public Sum(String name, String key, int resultIndex) {
		this.name = name;
		this.key = key;
		this.resultIndex = resultIndex;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public void addRow(ResultSet resultSet, RowSet row) throws SQLException {
		
		values.put(resultSet, (Number) row.getObject(resultIndex));
	}

	private void doSum(Number item) { 
		if(item==null){
			return;
		}
		if(value==null){
			value=item;
			return;
		}
		if (item instanceof Integer || item.getClass() == Integer.TYPE) {
			value = (Integer) value + (Integer) item;
			return;
		}
		if (item instanceof Long || item.getClass() == Long.TYPE) {
			value = (Long) value + (Long) item;
			return;
		}
		if (item instanceof Byte || item.getClass() == Byte.TYPE) {
			value = (Byte) value + (Byte) item;
			return;
		}
		if (item instanceof Double || item.getClass() == Double.TYPE) {
			value = (Double) value + (Double) item;
			return;
		}
		if (item instanceof Double || item.getClass() == Double.TYPE) {
			value = (Double) value + (Double) item;
			return;
		}
		if (item instanceof Double || item.getClass() == Double.TYPE) {
			value = (Double) value + (Double) item;
			return;
		}
		if (item instanceof Float || item.getClass() == Float.TYPE) {
			value = (Float) value + (Float) item;
			return;
		}
		if (item instanceof Short || item.getClass() == Short.TYPE) {
			value = (Short) value + (Short) item;
			return;
		}
		if (item instanceof BigDecimal) {
			value=((BigDecimal) value).add((BigDecimal) item);
			return;
		}
		if (item instanceof BigInteger) {
			value=((BigInteger) value).add((BigInteger) item);
			return;
		}
		// if AtomicLong 等等
		value = value.floatValue() + item.floatValue();

	}

	@Override
	public Number value() throws SQLException {
		if (value == null && !values.isEmpty()) {
			Collection<Number> collections=values.values();
			for (Number row : collections) {
				this.doSum((Number) row);
			}
		}
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
