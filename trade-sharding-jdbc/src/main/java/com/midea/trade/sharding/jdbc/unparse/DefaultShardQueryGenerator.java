package com.midea.trade.sharding.jdbc.unparse;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.TableName;
import com.midea.trade.sharding.core.resources.NameNodeHolder;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.ShardQueryGenerator;
import com.midea.trade.sharding.core.shard.TableInfo;



/**
 * DefaultShardQueryGenerator
 */
public class DefaultShardQueryGenerator implements ShardQueryGenerator {
	static Logger logger = LoggerFactory
			.getLogger(DefaultShardQueryGenerator.class);
	NodeTreeToSql sqlGenerator = new NodeTreeToSql();

	@Override
	public String generate(NameNodeHolder nameNode, AnalyzeResult analyzeResult) {

		Iterator<TableInfo> iterator = analyzeResult.getTableInfos().iterator();
		while (iterator.hasNext()) {
			TableInfo tableInfo = iterator.next();
			if (tableInfo.getOrgName() != null
					&& tableInfo.getOrgName().trim().length() > 0&&tableInfo.getOrgName().equalsIgnoreCase(nameNode.getOrgTableName())) {
				
				
				TableName tableName = (TableName) tableInfo.getTableNode();
				String schema = tableInfo.getSchema();
				if (nameNode.getSchema() != null) {
					schema = nameNode.getSchema();
				}
				tableName.init(schema, nameNode.getTableName());
			}
		}
		try {
			String sql = sqlGenerator.toString((QueryTreeNode) analyzeResult
					.getTreeNode());
			reset(analyzeResult.getTableInfos().iterator());// 重置
			return sql;
		} catch (StandardException e) {
			logger.error("sql StandardException,limit generate!", e);
		}
		// TODO Auto-generated method stub
		return null;
	}

	private void reset(Iterator<TableInfo> iterator) {
		while (iterator.hasNext()) {
			TableInfo tableInfo = iterator.next();
			if (tableInfo.getOrgName() != null
					&& tableInfo.getOrgName().trim().length() > 0) {
				TableName tableName = (TableName) tableInfo.getTableNode();
				tableName.init(tableInfo.getSchema(), tableInfo.getOrgName());
			}
		}
	}

}
