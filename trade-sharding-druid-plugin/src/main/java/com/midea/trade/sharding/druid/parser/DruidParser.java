package com.midea.trade.sharding.druid.parser;
import java.sql.SQLNonTransientException;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.druid.node.DruidAnalyzeResult;
import com.midea.trade.sharding.druid.node.DruidShardingParseInfo;
import com.midea.trade.sharding.druid.route.RouteResultset;
import com.midea.trade.sharding.druid.visitor.DruidSchemaStatVisitor;

/**
 * 对SQLStatement解析
 * 主要通过visitor解析和statement解析：有些类型的SQLStatement通过visitor解析足够了，
 *  有些只能通过statement解析才能得到所有信息
 *  有些需要通过两种方式解析才能得到完整信息
 */
public interface DruidParser {
	
	/**
	* @author chejy 
	* @Description: 使用DruidSchemaStatVisitor解析,得到tables、tableAliasMap、conditions等
	* @param configurations
	* @param rrs
	* @param stmt
	* @param schemaStatVisitor
	* @throws SQLNonTransientException
	* @return void    返回类型
	* @throws
	 */
	public void parser(Configurations configurations, RouteResultset rrs, SQLStatement stmt,DruidSchemaStatVisitor schemaStatVisitor)throws SQLNonTransientException;
	
	
	
	/**
	 * statement方式解析
	 * 子类可覆盖（如果visitorParse解析得不到表名、字段等信息的，就通过覆盖该方法来解析）
	 * 子类覆盖该方法一般是将SQLStatement转型后再解析（如转型为MySqlInsertStatement）
	 */
	public void statementParse(Configurations configurations, RouteResultset rrs, SQLStatement stmt) throws SQLNonTransientException;

	
	
	/**
	* @author chejy 
	* @Description:	子类可覆盖（如果该方法解析得不到表名、字段等信息的，就覆盖该方法，覆盖成空方法，然后通过statementPparse去解析）
	*  通过visitor解析：有些类型的Statement通过visitor解析得不到表名、
	* @param rrs
	* @param stmt
	* @param visitor
	* @throws SQLNonTransientException
	* @return void    返回类型
	* @throws
	 */
	public void visitorParse(RouteResultset rrs, SQLStatement stmt,DruidSchemaStatVisitor visitor) throws SQLNonTransientException;
	

	
	/**
	 * 获取解析到的信息
	 * @return
	 */
	public DruidShardingParseInfo getCtx();
	
}
