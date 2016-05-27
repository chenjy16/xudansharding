package com.midea.trade.sharding.jdbc.nodes.dml;

import java.sql.SQLException;
import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.parser.InsertNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ResultColumnList;
import com.foundationdb.sql.parser.TableName;
import com.midea.trade.sharding.core.context.StatementType;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.shard.TableInfo;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.NodeHelper;

/**
 * SQL节点解析器:InsertNodeAnalyzer
 */
public class InsertNodeAnalyzer extends
		AbstractNodeAnalyzer<InsertNode, AnalyzeResult> {
	static Logger logger = LoggerFactory.getLogger(InsertNodeAnalyzer.class);
	int[] nodeTypes = { NodeTypes.INSERT_NODE };

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
	public AnalyzeResult doAnalyze(InsertNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(node);
		result.setStatementType(StatementType.INSERT);
		TableInfo info = createTableInfo(node.getTargetTableName());
		result.getTableInfos().add(info);
		this.analyzeAndMergeResult(result, node.getResultSetNode());
		ResultColumnList resultColumnList = node.getResultSetNode()
				.getResultColumns();
		if (resultColumnList != null) {
			AnalyzeResult resultCols = Analyzers.get(
					resultColumnList.getNodeType()).analyze(resultColumnList);
			result.addResultColumns(resultCols.getResultColumns());
		}
		ResultColumnList targetColumnList = node.getTargetColumnList();
		if (targetColumnList != null && !targetColumnList.isEmpty()) {
			Iterator<TableColumn> iteratorColumn = result.getResultColumns()
					.iterator();
			while (iteratorColumn.hasNext()) {
				TableColumn tableColumn = iteratorColumn.next();
				tableColumn.setTable(info.getName());
				tableColumn.setName(targetColumnList.get(
						tableColumn.getResultIndex() - 1).getName());
			}
		} else {
			Iterator<TableColumn> iteratorColumn = result.getResultColumns()
					.iterator();
			while (iteratorColumn.hasNext()) {
				TableColumn tableColumn = iteratorColumn.next();
				tableColumn.setTable(info.getName());
				int index = (tableColumn.getResultIndex() - 1);
				String columnName;
				try {
					columnName = NodeHelper.getColumnName(info, index);
					tableColumn.setName(columnName);
				} catch (SQLException e) {
					logger.error("get column name from metadata error!table info="+info, e);
				}

			}
		}
		return result;
	}

}
