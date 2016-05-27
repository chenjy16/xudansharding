package com.midea.trade.sharding.jdbc.nodes.value;

import java.util.LinkedList;

import java.util.List;

import com.foundationdb.sql.parser.LikeEscapeOperatorNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;

/**
 * SQL节点解析器:LikeOperatorNodeAnalyzer
 * 
 */
public class LikeOperatorNodeAnalyzer extends
		AbstractNodeAnalyzer<LikeEscapeOperatorNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.LIKE_OPERATOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(LikeEscapeOperatorNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(); 
		List<TableColumn> columns = new LinkedList<TableColumn>();
		TableColumn column = new TableColumn();
		column.setName(node.getReceiver().getColumnName());
		column.setTable(node.getReceiver().getTableName());
		column.setValue(node.getLeftOperand());
		
		columns.add(column);
		result.addConditionColumns(columns);
		return result;
	}
	
}
