package com.midea.trade.sharding.core.shard;

import java.util.Set;

import com.midea.trade.sharding.core.jdbc.ParameterCallback;



/**
 * sql执行信息
 */
public class SqlExecuteInfo {
	String executeSql;
	Set<ParameterCallback<?>> callbacks;

	public String getExecuteSql() {
		return executeSql;
	}

	public Set<ParameterCallback<?>> parameterCallbacks() {
		return callbacks;
	}

	public Set<ParameterCallback<?>> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(Set<ParameterCallback<?>> callbacks) {
		this.callbacks = callbacks;
	}

	public void setExecuteSql(String executeSql) {
		this.executeSql = executeSql;
	}
}
