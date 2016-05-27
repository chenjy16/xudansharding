package com.midea.trade.sharding.client.idgenerator;

/**
 * Config
 */
public class Config {
	
	int concurrentSize = 100000;
	
	long initValue = 0;
	
	int step = 1;
	
	String name;
	
	IdGeneratorType type = IdGeneratorType.SEQUENCE;

	public int getConcurrentSize() {
		return concurrentSize;
	}

	public void setConcurrentSize(int concurrentSize) {
		this.concurrentSize = concurrentSize;
	}

	public long getInitValue() {
		return initValue;
	}

	public void setInitValue(long initValue) {
		this.initValue = initValue;
	}

	public int getStep() {
		return step;
	}

	public void setStep(int step) {
		this.step = step;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public IdGeneratorType getType() {
		return type;
	}

	public void setType(IdGeneratorType type) {
		this.type = type;
	}

}
