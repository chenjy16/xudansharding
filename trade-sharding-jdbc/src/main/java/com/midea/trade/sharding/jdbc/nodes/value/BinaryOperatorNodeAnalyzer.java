package com.midea.trade.sharding.jdbc.nodes.value;

import com.foundationdb.sql.parser.BinaryOperatorNode;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ParameterNode;
import com.foundationdb.sql.parser.ValueNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.NodeHelper;

/**
 * SQL节点解析器:BinaryOperatorNodeAnalyzer
 * ==  
 * !=
 * >
 * >=
 * <
 * <=
 * 加减乘除求余
 */
public class BinaryOperatorNodeAnalyzer extends
		AbstractBinaryOperatorNode<BinaryOperatorNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.BINARY_EQUALS_OPERATOR_NODE,
			NodeTypes.BINARY_NOT_EQUALS_OPERATOR_NODE,
			NodeTypes.BINARY_GREATER_THAN_OPERATOR_NODE,
			NodeTypes.BINARY_GREATER_EQUALS_OPERATOR_NODE,
			NodeTypes.BINARY_LESS_THAN_OPERATOR_NODE,
			NodeTypes.BINARY_LESS_EQUALS_OPERATOR_NODE,
			NodeTypes.BINARY_PLUS_OPERATOR_NODE,
			NodeTypes.BINARY_TIMES_OPERATOR_NODE,
			NodeTypes.BINARY_DIVIDE_OPERATOR_NODE,
			NodeTypes.BINARY_DIV_OPERATOR_NODE,
			NodeTypes.BINARY_MINUS_OPERATOR_NODE, 
			NodeTypes.MOD_OPERATOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	/**
	 * 逻辑分析，当语句中存在一下情况 1.＝情况，则比较两边是否是符合规则，一个是constant类型的，一个是column类型的，符合则就行操作
	 */
	@Override
	public AnalyzeResult doAnalyze(BinaryOperatorNode node) {
		
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		ValueNode left = node.getLeftOperand();
		ValueNode right = node.getRightOperand();
		if (NodeTypes.BINARY_EQUALS_OPERATOR_NODE != node.getNodeType()) {// 假如是＝，则解析分库分表条件，其它在单库情况下大多数都是
			analyzeAndMergeResult(result, left);
			analyzeAndMergeResult(result, right);
			return result;
		}
		if (left.getNodeType() == NodeTypes.COLUMN_REFERENCE && right.getNodeType() == NodeTypes.COLUMN_REFERENCE) {
			TableColumn column = createTableColumn((ColumnReference) left,
					(ColumnReference) right);
			result.getConditionColumns().add(column);
			return result;
		}

		if (left.getNodeType() == NodeTypes.COLUMN_REFERENCE) {
			if (NodeHelper.isColumnValueNode(right)) {
				TableColumn column = createTableColumn((ColumnReference) left,right);
				result.getConditionColumns().add(column);
			} else {// 右边不符合，则对右边进行递归解析

				analyzeAndMergeResult(result, right);
			}
		} else if (right.getNodeType() == NodeTypes.COLUMN_REFERENCE) {
			if (NodeHelper.isColumnValueNode(left)) {
				
				TableColumn column = createTableColumn((ColumnReference) right,left);
				result.getConditionColumns().add(column);
				
			} else {// left不符合，则对left进行递归解析
				analyzeAndMergeResult(result, left);
			}
		} else {// 都不符合，两边都解析

			analyzeAndMergeResult(result, left);
			analyzeAndMergeResult(result, right);
		}

		return result;
	}

	private TableColumn createTableColumn(ColumnReference columnRef,
			ColumnReference valueNode) {
		TableColumn column = new TableColumn();
		TableColumn joiCcolumn = new TableColumn();
		column.setValue(joiCcolumn);
		//joiCcolumn.setValue(column);
		column.setTable(columnRef.getTableName());
		column.setSchema(columnRef.getSchemaName());
		column.setName(columnRef.getColumnName());
		joiCcolumn.setTable(valueNode.getTableName());
		joiCcolumn.setSchema(valueNode.getSchemaName());
		joiCcolumn.setName(valueNode.getColumnName());
		return column;
	}

	
	private TableColumn createTableColumn(ColumnReference columnRef,
			ValueNode valueNode) {
		TableColumn column = new TableColumn();
		column.setValue(NodeHelper.getValue(valueNode));
		if (NodeHelper.isParameterNode(valueNode)) {
			column.setPreparedIndex(NodeHelper
					.getIndex((ParameterNode) valueNode));
		}
		column.setTable(columnRef.getTableName());
		column.setSchema(columnRef.getSchemaName());
		column.setName(columnRef.getColumnName());
		return column;
	}
}
