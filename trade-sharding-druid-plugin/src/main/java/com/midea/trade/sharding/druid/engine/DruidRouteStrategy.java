package com.midea.trade.sharding.druid.engine;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.midea.trade.sharding.druid.visitor.DruidSchemaStatVisitor;



public class DruidRouteStrategy {
	
	public void routeNormalSqlWithAST() {
		SQLStatementParser parser = new MySqlStatementParser("sql");
		DruidSchemaStatVisitor visitor = null;
		SQLStatement statement;
		/**
		 * 解析出现问题统一抛SQL语法错误
		 */
		try {
			statement = parser.parseStatement();
            visitor =new DruidSchemaStatVisitor();
		} catch (Exception t) {
		}
		
		
		
	}
	
	

}