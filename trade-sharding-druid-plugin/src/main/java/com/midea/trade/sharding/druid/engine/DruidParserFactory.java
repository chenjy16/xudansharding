package com.midea.trade.sharding.druid.engine;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.visitor.SchemaStatVisitor;
import com.midea.trade.sharding.druid.parser.DruidParser;
import com.midea.trade.sharding.druid.parser.impl.DefaultDruidParser;
import com.midea.trade.sharding.druid.parser.impl.DruidSelectParser;

public class DruidParserFactory {
	  public static DruidParser create(SQLStatement statement, SchemaStatVisitor visitor){
	        DruidParser parser = null;
	        if (statement instanceof SQLSelectStatement){
	           parser = new DruidSelectParser();
	        }else{
	            parser = new DefaultDruidParser();
	        }
	        return parser;
	    }
}
