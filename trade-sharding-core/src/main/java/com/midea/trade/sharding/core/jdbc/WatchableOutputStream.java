package com.midea.trade.sharding.core.jdbc;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
 

/**
 * WatchableOutputStream
 */
class WatchableOutputStream extends ByteArrayOutputStream {

	private OutputStreamWatcher watcher;
	
	
	/**
	 * @see java.io.OutputStream#close()
	 */
	public void close() throws IOException {
		super.close();
		if (this.watcher != null) {
			this.watcher.streamClosed(this);
		}
	}

	/**
	 * @param watcher
	 */
	public void setWatcher(OutputStreamWatcher watcher) {
		this.watcher = watcher;
	}
}