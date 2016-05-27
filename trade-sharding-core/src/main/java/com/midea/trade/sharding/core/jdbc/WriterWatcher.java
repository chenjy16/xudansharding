package com.midea.trade.sharding.core.jdbc;


/**
 * WriterWatcher
 */
interface WriterWatcher {
	/**
	 * Called when the Writer being watched has .close() called
	 */
	void writerClosed(WatchableWriter out);
}