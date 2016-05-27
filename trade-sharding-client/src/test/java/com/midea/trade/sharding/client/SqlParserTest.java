package com.midea.trade.sharding.client;

import org.junit.Test;

import com.midea.trade.sharding.jdbc.builder.DefaultStatementContextBuilder;


/**
 * 测试sql解析
 */
public class SqlParserTest {
	
	
	@Test
	public void queryTest() throws Exception{
		DefaultStatementContextBuilder builder = new DefaultStatementContextBuilder();
	/*	String sql = "select * from tablename as n,tablename2 n2 where n.col=1 and (n.c=n2.b or a=a) or 1=1";
		builder.build(sql, null);*/
		String sql2="select avg(shop_id) from  m_shop  group by shop_id";
		String sql = "select count(n.c) from tablename as n,tablename2 n2 where n.col=? and (n.c=n2.b or a=a) or 1=1 group by n.c";
		builder.build(sql2, null);
	/*	sql = "delete from tablename a";
		builder.build(sql, null);
		sql = "update tablename set n='a'";
		builder.build(sql, null);
		sql = "insert into tablename (n) values ('a')";
		builder.build(sql, null);*/
		
	}

}
