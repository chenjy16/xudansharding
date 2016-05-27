package com.midea.trade.sharding.core.script;

import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * javascript 编译器
 */
public class JavaScriptCompiler {
	static final ScriptEngineManager manager = new ScriptEngineManager();

	public static CompiledScript compile(String script, String lang)
			throws ScriptException {
		final ScriptEngine engine = manager.getEngineByName(lang);
		Compilable compilingEngine = (Compilable) engine;
		CompiledScript compiledScript = compilingEngine.compile(script);
		return compiledScript;
	}

}
