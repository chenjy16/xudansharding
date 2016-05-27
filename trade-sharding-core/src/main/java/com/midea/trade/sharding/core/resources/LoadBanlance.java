package com.midea.trade.sharding.core.resources;

/**
 * 负载类型
 */
public enum LoadBanlance {
	
	POLL("POLL"),
	
	POLL_WEIGHT("POLL_WEIGHT"),
	
	RANDOM("RANDOM"),
	
	RANDOM_WEIGHT("RANDOM_WEIGHT"),
	
	HA_RANDOM("HA_RANDOM"),
	
	HA_RANDOM_WEIGHT("HA_RANDOM_WEIGHT");
	
	private String input;
	
	private LoadBanlance(String input) {
		this.input = input;
	}
	
	public static LoadBanlance parse(String input) {
		if(input==null || input.length()<1)
			return HA_RANDOM_WEIGHT;
		
		for(LoadBanlance loadBanlance : values()){
			if(loadBanlance.input.equals(input)
					|| loadBanlance.name().equals(input)){
				return loadBanlance;
			}
		}
		throw new IllegalArgumentException("Unsupport LoadBanlance, input string : " + input);
	}

}
