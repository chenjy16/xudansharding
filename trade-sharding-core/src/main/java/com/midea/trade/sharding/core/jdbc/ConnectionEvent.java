package com.midea.trade.sharding.core.jdbc;

public enum ConnectionEvent {
	SET_READ_ONLY, SET_ISOLATE_LEVEL, SET_AUTO_COMMIT, SET_CATALOG, SET_TYPE_MAP, SET_HOLDABILITY,
	/**
	  * 以下是一个statement执行完毕后需要清除的，不是属性的设置，而且该场境，仅支持单库单表路由的情况
	  */
	CREATE_CLOB, CREATE_NCLOB, CREATE_BLOB, CREATE_SQL_XML;

}
