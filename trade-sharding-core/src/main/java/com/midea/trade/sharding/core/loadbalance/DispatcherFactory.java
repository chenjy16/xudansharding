package com.midea.trade.sharding.core.loadbalance;
import com.midea.trade.sharding.core.loadbalance.ha.SwapRandomDispatcher;
import com.midea.trade.sharding.core.loadbalance.ha.SwapRandomWeightDispatcher;
import com.midea.trade.sharding.core.resources.NameNode;

/**
 * 分发器工厂
 */
public class DispatcherFactory {
	
	static final Dispatcher random = new RandomDispatcher();
	static final Dispatcher swapRandom = new SwapRandomDispatcher();
	static final Dispatcher swapRandomWeight = new SwapRandomWeightDispatcher();
	static final Dispatcher randomWeight = new RandomWeightDispatcher();

	public static Dispatcher create(NameNode nameNode) {
		switch (nameNode.getLoadBanlance()) {
			case HA_RANDOM_WEIGHT:
				return swapRandomWeight;
				
			case HA_RANDOM:
				return swapRandom;
				
			case RANDOM:
				return random;
				
			case RANDOM_WEIGHT:
				return randomWeight;
				
			default:
				return swapRandomWeight;
		}
	}
}
