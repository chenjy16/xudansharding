package com.midea.trade.sharding.jdbc.check;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;

/**
 * Having语句检查
 */
public class HavingChecker implements Checker {

	@Override
	public boolean check(AnalyzeResult result) throws ShardException {
		if (result.getHavingInfo() == null) {
			return true;
		}
		Collection<TableColumn> resultColumns = result.getResultColumns();
		Collection<TableColumn> havingColumns = result.getHavingInfo()
				.getAggregateColumns();
		Map<String, TableColumn> columnsMap = new HashMap<String, TableColumn>();
		for (TableColumn column : resultColumns) {
			String key = column.getAggregate() + column.getName();
			columnsMap.put(key.toUpperCase(), column);
			if (column.getAliasName() != null
					&& column.getAliasName().trim().length() > 0) {
				key = column.getAliasName();
				columnsMap.put(key.toUpperCase(), column);
			}
		}
		for (TableColumn column : havingColumns) {
			String key = null;
			if (column.getAliasName() != null
					&& column.getAliasName().trim().length() > 0) {
				key = column.getAliasName().toUpperCase();
				if (!columnsMap.containsKey(key)) {
					throw new ShardException(
							"having error!result not include column=" + column);
				}
				continue;
			}
			key = column.getAggregate() + column.getName();
			if (!columnsMap.containsKey(key)) {
				throw new ShardException(
						"having error!result not include column=" + column);
			}
		}
		return true;
	}

}
