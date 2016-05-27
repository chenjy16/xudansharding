package com.midea.trade.sharding.jdbc.nodes.value;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.InListOperatorNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ParameterNode;
import com.foundationdb.sql.parser.ValueNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.NodeHelper;

/**
 * SQL节点解析器:InListOperatorNodeAnalyzer
 * 
 */
public class InListOperatorNodeAnalyzer extends
		AbstractNodeAnalyzer<InListOperatorNode, AnalyzeResult> {
	static Logger logger=LoggerFactory.getLogger(InListOperatorNodeAnalyzer.class);
	int[] nodeTypes = { NodeTypes.IN_LIST_OPERATOR_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	/**
	 * 将in查询解析分解成N个，根据这n个值进行路由，假如存在子查询，则继续解析
	 */
	@Override
	public AnalyzeResult doAnalyze(InListOperatorNode valueNode) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		List<TableColumn> columns = new LinkedList<TableColumn>();
		Iterator<ValueNode> valuesIterator = valueNode.getRightOperandList()
				.getNodeList().iterator();
		while (valuesIterator.hasNext()) {
			ValueNode item = valuesIterator.next();
			if (!NodeHelper.isColumnValueNode(item)) {
				this.analyzeAndMergeResult(result, item);
				continue;
			}
			TableColumn column = new TableColumn();
			columns.add(column);
			initColumnValue(column, valueNode.getLeftOperand().getNodeList()
					.get(0));
			column.setValue(NodeHelper.getValue(item));
			if (item instanceof ParameterNode) {
				ParameterNode parNode = (ParameterNode) item;
				column.setPreparedIndex(parNode.getParameterNumber()+1);
			}
		}
		result.addConditionColumns(columns);
		return result;
	}

	public static void initColumnValue(TableColumn column, ValueNode valueNode) {
		column.setName(valueNode.getColumnName());
		column.setTable(valueNode.getTableName());
		try {
			column.setSchema(valueNode.getSchemaName());
		} catch (StandardException e) {
			logger.error("sql StandardException!",e);
		}
	}
}
