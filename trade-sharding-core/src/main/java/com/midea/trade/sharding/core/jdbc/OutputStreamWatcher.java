
package com.midea.trade.sharding.core.jdbc;


/**
 * Output Stream 观察者约束
 */
public interface OutputStreamWatcher {
	
	/**
	 * Called when the OutputStream being watched has .close() called
	 */
	void streamClosed(WatchableOutputStream out);
	
}
