package com.midea.trade.sharding.druid.expr;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;
public class SQLQueryExprAnalyzer {
	
	static Logger logger = LoggerFactory.getLogger(SQLQueryExprAnalyzer.class);
	
	public AnalyzeResult doAnalyze(SQLQueryExpr node){
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(node);
		result.setStatementType(StatementType.SELECT);
		/*if(node.getSubQuery().getWithSubQuery()!=null){
			SQLWithSubqueryClause subQuery=node.getSubQuery().getWithSubQuery();
			List<Entry> list=subQuery.getEntries();
			for(Entry entry:list){
				SQLSelect select=entry.getSubQuery();
			}
		}*/
		
		MySqlSelectQueryBlock query = (MySqlSelectQueryBlock) node.getSubQuery().getQuery();
		
		SQLTableSource  sqlTableSource=query.getFrom();
		
		SQLOrderBy orderBy=query.getOrderBy();
		
		SQLSelectGroupByClause  groupBy=query.getGroupBy();
		
		List<SQLSelectItem>  list=query.getSelectList();
		
		SQLExpr where =query.getWhere();
		return null;
	}

}
