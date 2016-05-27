package com.midea.trade.sharding.core.shard;

/**
 * 脚本实体描述
 */
public class Script {
	
	String content;
	
	String language;
	
	String execute;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	

	public String getExecute() {
		return execute;
	}

	public void setExecute(String execute) {
		this.execute = execute;
	}

	@Override
	public String toString() {
		return "Script [language=" + language + " content=" + content + "]";
	}

}
