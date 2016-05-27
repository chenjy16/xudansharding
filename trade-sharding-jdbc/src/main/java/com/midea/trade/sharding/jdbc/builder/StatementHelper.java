package com.midea.trade.sharding.jdbc.builder;

import java.util.ArrayList;
import java.util.List;

import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.SQLParser;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableInfo;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.NodeAnalyzer;

/**
 * StatementHelper
 */
public abstract class StatementHelper {

	static final ThreadLocal<SQLParser> threadLocal = new ThreadLocal<SQLParser>();

	public static SQLParser createSQLParser() {
		SQLParser parser = threadLocal.get();
		if (parser == null) {
			parser = new SQLParser();
			threadLocal.set(parser);
		}
		return parser;
	}

	public static List<TableInfo> getTableNames(QueryTreeNode queryNode) {
		NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers.get(queryNode);
		AnalyzeResult result = analyzer.analyze(queryNode);
		return new ArrayList<TableInfo>(result.getTableInfos());
	}

}
