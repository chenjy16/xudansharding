package com.midea.trade.sharding.jdbc.unparse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.QueryTreeNode;

/**
 * ToStringBuilder
 */
public abstract class ToStringBuilder {
	static Logger logger = LoggerFactory.getLogger(ToStringBuilder.class);
	static final NodeTreeToSql nodeTreeToSql = new NodeTreeToSql();

	public static String toString(QueryTreeNode treeNode) {
		try {
			return nodeTreeToSql.toString(treeNode);
		} catch (StandardException e) {
			logger.error("toString error!", e);
		}
		return null;
	}
}
