package com.midea.trade.sharding.core.script;

import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * 脚本执行器
 */
public class InterpretedScriptExecutor<T> implements ScriptExecutor<T> {
	static final ScriptEngineManager manager = new ScriptEngineManager();

	@SuppressWarnings("unchecked")
	@Override
	public T execute(String script, Map<String, Object> parameters)
			throws ScriptException {
		ScriptEngine scriptEngine = manager.getEngineByName("js");
		Bindings binding = scriptEngine.createBindings();
		if (parameters != null) {
			binding.putAll(parameters);
		}
		Object val = scriptEngine.eval(script, binding);
		return ((T) val);
	}

	public static void main(String args[]) throws Exception {
		ScriptExecutor<Boolean> executor = new InterpretedScriptExecutor<Boolean>();
		String exp = "var result=(1 >0 && 2>99||1==1);result";
		Object value = executor.execute(exp, null);
		System.out.println("value=" + value);
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("a", "5");
		boolean result = executor.execute("a < 1", parameters);
		System.out.println(result);
	}
}
