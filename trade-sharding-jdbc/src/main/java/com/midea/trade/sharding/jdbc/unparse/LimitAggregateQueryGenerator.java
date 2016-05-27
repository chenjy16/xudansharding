package com.midea.trade.sharding.jdbc.unparse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.midea.trade.sharding.core.resources.NameNodeHolder;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.ShardQueryGenerator;


/**
 * LimitAggregateQueryGenerator
 * 
 */
public class LimitAggregateQueryGenerator implements ShardQueryGenerator {
	static Logger logger = LoggerFactory
			.getLogger(LimitAggregateQueryGenerator.class);
	NodeTreeToSql sqlGenerator = new NodeTreeToSql();

	@Override
	public String generate(NameNodeHolder nameNode, AnalyzeResult analyzeResult) {
		try {
			String sql = sqlGenerator.toString((QueryTreeNode) analyzeResult
					.getTreeNode());
			return sql;
		} catch (StandardException e) {
			logger.error("sql StandardException,limit generate!", e);
		}
		// TODO Auto-generated method stub
		return null;
	}

}
