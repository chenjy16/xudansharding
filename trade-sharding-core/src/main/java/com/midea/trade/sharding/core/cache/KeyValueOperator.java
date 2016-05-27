package com.midea.trade.sharding.core.cache;
import java.io.Serializable;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.shard.TableInfo;



public interface KeyValueOperator {

	String generateKey(TableInfo tableInfo, TableColumn[] updatedColumns,
			TableColumn[] conditionColumns);

	Serializable resolveValue(TableInfo tableInfo, TableColumn[] updatedColumns,
			TableColumn[] conditionColumns);
}
