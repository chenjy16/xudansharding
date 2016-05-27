package com.midea.trade.sharding.jdbc.builder;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.parser.MySqlStatementParser;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.core.context.DruidStatementContext;
import com.midea.trade.sharding.druid.engine.DruidParserFactory;
import com.midea.trade.sharding.druid.node.DruidShardingParseInfo;
import com.midea.trade.sharding.druid.parser.DruidParser;
import com.midea.trade.sharding.druid.visitor.DruidSchemaStatVisitor;

public class DruidStatementContextBuilder {

	static Logger logger = LoggerFactory.getLogger(DruidStatementContextBuilder.class);
	
	public DruidStatementContext build(String sql, DruidStatementContext context)
			throws SQLException {
		
		if (context == null) {
			context = new DruidStatementContext();
			DruidStatementContext.setContext(context);
			if (logger.isDebugEnabled()) {
				logger.debug("create context!sql=" + sql);
			}
		}
		//存储sql
		if (context.getCurrentBatch().getSql() == null) {
			context.getCurrentBatch().setSql(sql);
		}
		
		//TrackerExecutor.trackBegin(TrackPoint.PARSE_SQL, sql);
		SQLStatementParser parser = new MySqlStatementParser(sql); 
		SQLStatement statement = parser.parseStatement();
		DruidSchemaStatVisitor visitor = new DruidSchemaStatVisitor();
		DruidParser druidParser = DruidParserFactory.create(statement);
		Configurations configurations=Configurations.getInstance();
		druidParser.parser(configurations, statement, visitor);
		DruidShardingParseInfo ctx=druidParser.getCtx();
		//RouterUtil.tryRouteForTables(schema, druidParser.getCtx(), unit, rrs, isSelect(statement), cachePool);
		//TrackerExecutor.trackEnd(TrackPoint.PARSE_SQL);
		return context;
	}

}
