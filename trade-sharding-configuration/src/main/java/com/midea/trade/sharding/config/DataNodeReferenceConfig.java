package com.midea.trade.sharding.config;

/**
 * DataNode引用配置
 * 
 */
public class DataNodeReferenceConfig {
	
	private String ref;
	private AccessMode accessMode;
	private Long weight;
	
	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public AccessMode getAccessMode() {
		return accessMode;
	}

	public void setAccessMode(AccessMode accessMode) {
		this.accessMode = accessMode;
	}

	public Long getWeight() {
		return weight;
	}

	public void setWeight(Long weight) {
		this.weight = weight;
	}
	

}
