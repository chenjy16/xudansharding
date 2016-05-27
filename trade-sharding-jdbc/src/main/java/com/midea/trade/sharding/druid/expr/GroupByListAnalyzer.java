package com.midea.trade.sharding.druid.expr;

import java.util.List;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.midea.trade.sharding.core.shard.AnalyzeResult;

/**
 * SQL节点解析器:GroupByListAnalyzer
 */
public class GroupByListAnalyzer  {


	public AnalyzeResult doAnalyze(SQLSelectGroupByClause node) {
		List<SQLExpr> list=node.getItems();
		
		return null;
	}

}
