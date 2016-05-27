package com.midea.trade.sharding.config;

/**
 * Include配置实体
 * 
 */
public class IncludeConfig implements Config {
	
	private static final long serialVersionUID = 1L;
	
	String file;

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	@Override
	public String toString() {
		return "IncludeConfig [file=" + file + "]";
	}

}
