package com.midea.trade.sharding.druid.expr;
import java.util.List;

import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.midea.trade.sharding.core.shard.AnalyzeResult;

/**
 * SQL节点解析器:OrderByListAnalyzer
 */
public class OrderByListAnalyzer {

	public AnalyzeResult doAnalyze(SQLOrderBy node) {
		List<SQLSelectOrderByItem> orderByItems = node.getItems();
    	for(SQLSelectOrderByItem orderByItem : orderByItems){
    	}
		return null;
	}

}
