package com.midea.trade.sharding.config;

import com.midea.trade.sharding.core.shard.Script;


/**
 * Function配置实体
 */
public class FunctionConfig implements Config {

	private static final long serialVersionUID = 1L;
	
	String ref;
	String execute;
	Script script;

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getExecute() {
		return execute;
	}

	public void setExecute(String execute) {
		this.execute = execute;
	}

	public Script getScript() {
		return script;
	}

	public void setScript(Script script) {
		this.script = script;
	}
	
	public static FunctionConfig NO_SHARD_FUNCTION_CONFIG = new FunctionConfig();

}
