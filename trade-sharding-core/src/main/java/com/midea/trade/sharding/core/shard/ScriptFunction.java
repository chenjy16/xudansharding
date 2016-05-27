package com.midea.trade.sharding.core.shard;

import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptException;

import com.midea.trade.sharding.core.exception.ConfigurationException;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.script.CompiledJavaScriptExecutor;
import com.midea.trade.sharding.core.script.ScriptExecutor;

/**
 * 根据script配置来获取下标

 */
public class ScriptFunction implements Function {
	final ScriptExecutor<Double> scriptExecutor;

	public ScriptFunction(Script script) {
		try {
			String function = null;
			if (script.getExecute() != null&& script.getExecute().trim().length() > 0) {
				function = script.getExecute();
			} else {
				function = " function result(){" + script.getContent()+ "};result();";
			}
			scriptExecutor = new CompiledJavaScriptExecutor<Double>(function,script.getLanguage());
		} catch (Exception e) {
			throw new ConfigurationException("script config error!script="
					+ script, e);
		}
	}

	@Override
	public int execute(int size, Map<String, Object> parameters) {

		try {
			Map<String, Object> context = new HashMap<String, Object>();
			for (Map.Entry<String, Object> entry : parameters.entrySet()) {
				context.put("$" + entry.getKey(), entry.getValue());
				context.put("$" + entry.getKey().toUpperCase(),
						entry.getValue());
				context.put("$" + entry.getKey().toLowerCase(),
						entry.getValue());
			}
			context.put("$NODE_SIZE", size);
			Double value = scriptExecutor.execute(null,context);
			if (value != null) {
				return value.intValue();
			} else {
				throw new ShardException("function error,could not find namenodes index="+value+"!parameters=" + parameters);
			}
		} catch (ScriptException e) {
			throw new ShardException("script error!parameters=" + parameters, e);
		}

	}
}
