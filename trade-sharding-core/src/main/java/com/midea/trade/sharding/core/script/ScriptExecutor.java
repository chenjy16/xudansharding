package com.midea.trade.sharding.core.script;

import java.util.Map;

import javax.script.ScriptException;

/**
 * 脚本执行器约束
 */
public interface ScriptExecutor<T> {
	
	public T execute(String script,Map<String,Object> parameters) throws ScriptException;

}
