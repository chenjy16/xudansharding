package com.midea.trade.sharding.core.jdbc.aggregate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.midea.trade.sharding.core.jdbc.result.RowSet;


/**
 * 平均值
 */
public class Avg implements Aggregate<Number> {
	private String name;
	private String key;
	private Sum sum;
	private Count count;
	private final int resultIndex;

	public Avg(Sum sum, Count count, String key, int resultIndex) {
		this.sum = sum;
		this.count = count;
		this.name = sum.getColumnName();
		this.key = key;
		this.resultIndex = resultIndex;
	}

	@Override
	public String key() {
		return key;
	}

	@Override
	public void addRow(ResultSet result, RowSet item) {

	}

	@Override
	public Number value() throws SQLException {
		Number sumValue=sum.value();
		if(sumValue==null){
			return null;
		}
		BigDecimal sumDecimal=new BigDecimal(sumValue.toString()),
				countDecimal=new BigDecimal(count.value().toString());
		return sumDecimal.divide(countDecimal,4,RoundingMode.FLOOR);
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
