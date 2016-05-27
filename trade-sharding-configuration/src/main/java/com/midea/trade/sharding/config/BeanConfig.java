package com.midea.trade.sharding.config;

/**
 * Bean配置
 */
public class BeanConfig implements Config {
	
	private static final long serialVersionUID = 1L;
	
	String id;
	String className;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public String toString() {
		return "BeanConfig [id=" + id + ", className=" + className + "]";
	}
	

}
