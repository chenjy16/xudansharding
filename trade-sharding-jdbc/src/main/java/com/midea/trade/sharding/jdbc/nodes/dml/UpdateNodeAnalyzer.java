package com.midea.trade.sharding.jdbc.nodes.dml;

import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ResultColumnList;
import com.foundationdb.sql.parser.TableName;
import com.foundationdb.sql.parser.UpdateNode;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableInfo;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:UpdateNodeAnalyzer
 */
public class UpdateNodeAnalyzer extends
		AbstractNodeAnalyzer<UpdateNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.UPDATE_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	TableInfo createTableInfo(TableName tableName) {
		TableInfo info = new TableInfo();
		try {
			info.setSchema(tableName.getSchemaName());
			info.setOrgName(tableName.getTableName());
			info.setName(tableName.getTableName());
			info.setTableNode(tableName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}

	@Override
	public AnalyzeResult doAnalyze(UpdateNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(node);
		result.setStatementType(StatementType.UPDATE);
		// TableInfo info = createTableInfo(node.getTargetTableName());
		// result.getTableInfos().add(info);
		this.analyzeAndMergeResult(result, node.getResultSetNode());
		ResultColumnList resultColumnList = node.getResultSetNode()
				.getResultColumns();
		if (resultColumnList != null) {
			AnalyzeResult resultCols = Analyzers.get(
					resultColumnList.getNodeType()).analyze(resultColumnList);
			result.addResultColumns(resultCols.getResultColumns());
		}
		return result;
	}

}
