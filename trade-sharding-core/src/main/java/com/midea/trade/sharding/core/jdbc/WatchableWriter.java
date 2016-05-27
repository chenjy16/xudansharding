package com.midea.trade.sharding.core.jdbc;

import java.io.CharArrayWriter;

/**
 * WatchableWriter
 */
class WatchableWriter extends CharArrayWriter {

	private WriterWatcher watcher;
	/**
	 * @see java.io.Writer#close()
	 */
	public void close() {
		super.close();
		if (this.watcher != null) {
			this.watcher.writerClosed(this);
		}
	}

	/**
	 * @param watcher
	 */
	public void setWatcher(WriterWatcher watcher) {
		this.watcher = watcher;
	}
}
