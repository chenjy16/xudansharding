package com.midea.trade.sharding.core.jdbc.result;

import java.util.Comparator;

import com.google.common.collect.ComparisonChain;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.shard.OrderByType;
import com.midea.trade.sharding.core.shard.TableColumn;

/**
 * RowSet 比较器
 */
public class RowSetComparator implements Comparator<RowSet> {
	
	private final TableColumn sortedColumns[];

	public RowSetComparator(TableColumn sortedColumns[]) {
		this.sortedColumns = sortedColumns;
	}

	@Override
	public int compare(RowSet o1, RowSet o2) {
		ComparisonChain chain = ComparisonChain.start();
		try {
			for (int i = 0; i < sortedColumns.length; i++) {
				TableColumn column = sortedColumns[i];
				OrderByType orderByType = column.getOrderByType();
				Object target1 = o1.getObject(column.getResultIndex());
				Object target2 = o2.getObject(column.getResultIndex());
				if (target1 == null) {// 将null值转化为string进行处理
					target1 = "";
					if (target2 != null) {
						target2 = target2.toString();
					} else {
						target2 = "";
					}
				} else if (target2 == null) {
					target2 = "";
					target1 = target1.toString();
				}

				if (orderByType == null || orderByType == OrderByType.ASC) {
					chain=chain.compare((Comparable<?>) target1, (Comparable<?>) target2);
				} else {
					chain=chain.compare((Comparable<?>) target2, (Comparable<?>) target1);
				}

			}
		} catch (Exception e) {
			throw new ShardException("compare error!row=" + this, e);
		}
		return chain.result();
	}

}
