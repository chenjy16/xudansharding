package com.midea.trade.sharding.core.cycle;

import java.sql.SQLException;

/**
 * LifeCycle 约束
 */
public interface LifeCycle {

	public void start() throws SQLException;

	public void close() throws SQLException;

	public void destroy() throws SQLException;

}
