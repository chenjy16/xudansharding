package com.midea.trade.sharding.core.script;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * javascript 执行器
 */
@SuppressWarnings("unchecked")
public class CompiledJavaScriptExecutor<T> implements ScriptExecutor<T> {
	
	static Logger logger = LoggerFactory.getLogger(CompiledJavaScriptExecutor.class);
	final String script;
	final CompiledScript compiledScript;

	static String systemFunction = null;

	static {
		InputStream stream = null;
		BufferedReader reader = null;
		try {

			stream = CompiledJavaScriptExecutor.class
					.getResourceAsStream("functions.js");
			BufferedInputStream bs = new BufferedInputStream(stream);
			reader = new BufferedReader(new InputStreamReader(bs));
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			systemFunction = sb.toString();

		} catch (Exception e) {
			logger.error("classpath get file error!", e);

		} finally {
			try {
				if (stream != null) {
					stream.close();
				}
				if (reader != null) {
					reader.close();
				}
			} catch (Exception e) {
				logger.error("classpath get file error!", e);

			}
		}
	}

	public CompiledJavaScriptExecutor(String script) throws ScriptException,
			IOException {
		this(script, "js");
	}

	public CompiledJavaScriptExecutor(String script, String lang)
			throws ScriptException {
		if (lang == null || lang.trim().length() <= 0) {
			lang = "js";
		}
		this.script = script;
		StringBuilder functionScript = new StringBuilder();
		functionScript.append(systemFunction);
		functionScript.append(script);
		this.compiledScript = JavaScriptCompiler.compile(
				functionScript.toString(), lang);
	}

	@Override
	public T execute(String script,Map<String, Object> parameters) throws ScriptException {
		Bindings bindings = compiledScript.getEngine().createBindings();
		if (parameters != null) {
			bindings.putAll(parameters);
		}

		T result = (T) compiledScript.eval(bindings);
		return result;
	}

}
