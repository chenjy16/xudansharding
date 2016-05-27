package com.midea.trade.sharding.druid.expr;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSubqueryTableSource;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUnionQueryTableSource;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.midea.trade.sharding.core.shard.AnalyzeResult;



/**
 * SQL节点解析器:FromListNodeAnalyzer
 */
public class FromListNodeAnalyzer {

	public AnalyzeResult doAnalyze(SQLTableSource node) {
		if(node instanceof  SQLJoinTableSource){
			SQLJoinTableSource joinFrom=(SQLJoinTableSource)node;
		}else if(node instanceof SQLSubqueryTableSource){
			SQLSubqueryTableSource subQueryFrom=(SQLSubqueryTableSource)node;
		}else if(node instanceof  SQLUnionQueryTableSource){
			SQLUnionQueryTableSource unionQueryFrom=(SQLUnionQueryTableSource)node;
		}
		return null;
	}
	


}
