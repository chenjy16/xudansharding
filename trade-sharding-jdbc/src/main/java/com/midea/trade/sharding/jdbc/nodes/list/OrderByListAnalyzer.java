package com.midea.trade.sharding.jdbc.nodes.list;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.OrderByColumn;
import com.foundationdb.sql.parser.OrderByList;
import com.foundationdb.sql.parser.ValueNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.OrderByType;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;


/**
 * SQL节点解析器:OrderByListAnalyzer
 */
public class OrderByListAnalyzer extends
		AbstractNodeAnalyzer<OrderByList, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.ORDER_BY_LIST };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(OrderByList node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		for (OrderByColumn column : node) {

			ValueNode valueNode = column.getExpression();
			if (valueNode instanceof ColumnReference) {
				ColumnReference colRef = (ColumnReference) valueNode;
				TableColumn tableColumn = new TableColumn();
				tableColumn.setName(colRef.getColumnName());
				if (column.isAscending()) {
					tableColumn.setOrderByType(OrderByType.ASC);
				} else {
					tableColumn.setOrderByType(OrderByType.DESC);
				}
				tableColumn.setSchema(colRef.getSchemaName());
				tableColumn.setTable(colRef.getTableName());
				result.getOrderByColumns().add(tableColumn);
			}

		}
		return result;
	}

}
