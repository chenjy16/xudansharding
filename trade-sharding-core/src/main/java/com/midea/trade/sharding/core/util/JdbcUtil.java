package com.midea.trade.sharding.core.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * JdbcUtil
 * 
 */
public class JdbcUtil {
	
	protected static final Logger logger = LoggerFactory.getLogger(JdbcUtil.class);

	/**
	 * close a connection
	 * @param con
	 */
	public static void closeConnection(Connection conn) {
		try {
			if (conn == null || conn.isClosed())
				return;
			
			conn.close();
		} catch (SQLException ex) {
			logger.debug("Could not close JDBC Connection", ex);
		} catch (Throwable ex) {
			logger.debug("Unexpected exception on closing JDBC Connection", ex);
		}
	}

	/**
	 * close a statement
	 * @param stmt
	 */
	public static void closeStatement(Statement stmt) {
		if (stmt == null) {
			return;
		}
		try {
			stmt.close();
		} catch (SQLException ex) {
			logger.trace("Could not close JDBC Statement", ex);
		} catch (Throwable ex) {
			logger.trace("Unexpected exception on closing JDBC Statement", ex);
		}
	}

	/**
	 * close a ResultSet
	 * @param rs
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs == null) {
			return;
		}
		try {
			rs.close();
		} catch (SQLException ex) {
			logger.trace("Could not close JDBC ResultSet", ex);
		} catch (Throwable ex) {
			logger.trace("Unexpected exception on closing JDBC ResultSet", ex);
		}
	}
	
	/**
	 * close all
	 * @param rs
	 * @param stmt
	 * @param conn
	 */
	public static void close(ResultSet rs, Statement stmt, Connection conn) {
		closeResultSet(rs);
		closeStatement(stmt);
		closeConnection(conn);
	}
}
