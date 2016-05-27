package com.midea.trade.sharding.druid.expr;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.midea.trade.sharding.core.shard.AnalyzeResult;


/**
 * SQL节点解析器:ResultColumnListNodeAnalyzer
 */
public class ResultColumnListNodeAnalyzer {
	
	static final Logger logger = LoggerFactory.getLogger(ResultColumnListNodeAnalyzer.class);
	
	public AnalyzeResult doAnalyze(List<SQLSelectItem> node) {
		
		
		return null;
	}

}
