package com.midea.trade.sharding.core.exception.sqlexception;

import java.sql.SQLException;

/**
 * mysql 异常
 */
public final class MySqlException {

	private MySqlException() {
	}

	public static boolean isMysqlException(SQLException exception) {
		String sqlState = exception.getSQLState();
		if (sqlState != null && sqlState.startsWith("08")) { // per Mark Matthews at MySQL
			return true;
		}

		MySqlExceptionType type = MySqlExceptionType
				.getMySqlExceptionType(exception.getErrorCode());

		return type != null;
	}

}
