package com.midea.trade.sharding.jdbc.nodes.list;

import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.GroupByColumn;
import com.foundationdb.sql.parser.GroupByList;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ValueNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:GroupByListAnalyzer
 */
public class GroupByListAnalyzer extends
		AbstractNodeAnalyzer<GroupByList, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.GROUP_BY_LIST };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(GroupByList node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		for (GroupByColumn column : node) {
			ValueNode valueNode = column.getColumnExpression();
			if (valueNode instanceof ColumnReference) {
				ColumnReference colRef = (ColumnReference) valueNode;
				TableColumn tableColumn = new TableColumn();
				tableColumn.setName(colRef.getColumnName());
				tableColumn.setSchema(colRef.getSchemaName());
				tableColumn.setTable(colRef.getTableName());
				result.getGroupByColumns().add(tableColumn);
			}
		}
		return result;
	}

}
