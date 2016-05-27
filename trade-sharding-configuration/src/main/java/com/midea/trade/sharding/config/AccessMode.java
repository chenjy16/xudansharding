package com.midea.trade.sharding.config;

/**
 * 数据源的访问模式
 */
public enum AccessMode {
	
	READONLY("READONLY", true, false),
	
	WRITEONLY("WRITEONLY", false, true),
	
	READ_WRITE("READ-WRITE", true, true);

	private String input;
	
	private boolean canRead;
	
	private boolean canWrite;
	
	private AccessMode(String input, boolean canRead, boolean canWrite) {
		this.input = input;
		this.canRead = canRead;
		this.canWrite = canWrite;
	}

	public String getInput() {
		return input;
	}

	public boolean isCanRead() {
		return canRead;
	}

	public boolean isCanWrite() {
		return canWrite;
	}
	
	public static AccessMode parse(String input){
		for(AccessMode mode : values()){
			if(mode.getInput().equals(input)
					|| mode.name().equals(input)){
				return mode;
			}
		}
		throw new IllegalArgumentException("Unsupport AccessMode, input string : " + input);
	}
	
}
