package com.midea.trade_sharding_druid_plugin;

import org.junit.Test;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.util.JdbcConstants;

public class druidTest {
	
	@Test
	public void  druid(){
		SQLParserUtils.createExprParser("", JdbcConstants.MYSQL);
		SQLParserUtils.createSQLStatementParser("", JdbcConstants.MYSQL);
		SQLUtils.toMySqlExpr("");
		SQLUtils.toStatementList("", JdbcConstants.MYSQL);
		SQLUtils.parseStatements("", JdbcConstants.MYSQL);
		
	}

}
