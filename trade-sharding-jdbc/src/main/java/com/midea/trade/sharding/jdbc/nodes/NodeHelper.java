package com.midea.trade.sharding.jdbc.nodes;

import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.AggregateNode;
import com.foundationdb.sql.parser.AllResultColumn;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.ConstantNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ParameterNode;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.ValueNode;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.resources.NameNodeHolder;
import com.midea.trade.sharding.core.resources.TableDescription;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.shard.TableInfo;
import com.midea.trade.sharding.core.util.ConnectionUtils;
import com.midea.trade.sharding.jdbc.unparse.ToStringBuilder;

/**
 * NodeHelper
 */
public abstract class NodeHelper {
	static final Logger logger = LoggerFactory.getLogger(NodeHelper.class);

	public static String getColumnName(TableInfo info, int index)
			throws SQLException {
		String tableName = info.getOrgName();
		Configurations configurations = Configurations.getInstance();
		NameNodeHolder holder = configurations.findNameNode(info);
		if (holder == null) {
			TableDescription desc = configurations
					.getTableDescription(tableName);
			holder = desc.getNameNode(0);
		}
		DataNode node = holder.getDataNodes().get(0);

		TableColumn[] tableColumns = ConnectionUtils.getTableConlumns(node,
				tableName);
		TableColumn column = tableColumns[index];
		return column.getName();
	}

	public static TableColumn[] getColumns(TableInfo info) throws SQLException {
		Configurations configurations = Configurations.getInstance();
		NameNodeHolder holder = configurations.findNameNode(info);
		String tableName = info.getOrgName();
		if (holder == null) {
			TableDescription desc = configurations
					.getTableDescription(tableName);
			holder = desc.getNameNode(0);
		}
		DataNode node = holder.getDataNodes().get(0);
		TableColumn[] tableColumns = ConnectionUtils.getTableConlumns(node,
				holder.getTableName());
		return tableColumns;
	}

	public static Object getValue(ConstantNode node) {
		return node.getValue();
	}

	public static Object getValue(ParameterNode node) {
		return "?";
	}

	public static Object getValue(ValueNode node) {
		if (node instanceof ConstantNode) {
			return getValue((ConstantNode) node);
		}
		if (node instanceof ParameterNode) {
			return getValue((ParameterNode) node);
		}
		return null;
	}

	public static boolean isColumnValueNode(ValueNode node) {
		return node instanceof ConstantNode
				|| node.getNodeType() == NodeTypes.PARAMETER_NODE;
	}

	public static boolean isParameterNode(ValueNode node) {
		return node.getNodeType() == NodeTypes.PARAMETER_NODE;
	}

	public static int getIndex(ParameterNode node) {
		return node.getParameterNumber() + 1;
	}

	public static TableColumn parseResultColumn(ResultColumn resultColumn) {
		TableColumn tableColumn = new TableColumn();

		tableColumn.setResultIndex(resultColumn.getColumnPosition());
		if (resultColumn.getNodeType() == NodeTypes.ALL_RESULT_COLUMN) {
			tableColumn.setName("*");
			AllResultColumn all = (AllResultColumn) resultColumn;
			if (all.getTableNameObject() != null) {
				tableColumn.setTable(all.getTableNameObject().getTableName());
				tableColumn.setSchema(all.getTableNameObject().getSchemaName());
			}
		} else {

			tableColumn.setTable(resultColumn.getTableName());
			tableColumn.setAliasName(resultColumn.getName());

			try {
				tableColumn.setSchema(resultColumn.getSchemaName());
			} catch (StandardException e) {
				logger.error("exception when add resultColumn!result column="
						+ resultColumn.toString(), e);
			}
			if (resultColumn.getReference() != null) {
				initColumn(tableColumn, resultColumn.getReference());
			}
			if (resultColumn.getExpression() instanceof ColumnReference) {
				initColumn(tableColumn,
						(ColumnReference) resultColumn.getExpression());
			}
			if (resultColumn.getExpression() instanceof ConstantNode) {
				tableColumn.setValue(((ConstantNode) resultColumn
						.getExpression()).getValue());
			}
			if (resultColumn.getExpression() instanceof ParameterNode) {
				tableColumn.setPreparedIndex(((ParameterNode) resultColumn
						.getExpression()).getParameterNumber() + 1);
				tableColumn.setValue("?");
			}

		}
		if (resultColumn.getExpression() instanceof AggregateNode) {
			AggregateNode aggregateNode = (AggregateNode) resultColumn
					.getExpression();
			parseAggregate(tableColumn, aggregateNode);
		}
		return tableColumn;
	}

	public static TableColumn parseAggregate(AggregateNode aggregateNode) {
		TableColumn tableColumn = new TableColumn();
		parseAggregate(tableColumn, aggregateNode);
		return tableColumn;
	}

	private static void parseAggregate(TableColumn tableColumn,
			AggregateNode aggregateNode) {
		tableColumn.setAggregate(aggregateNode.getAggregateName());
		
		tableColumn.setAggregateNode(aggregateNode);
		
		tableColumn.setAggregateNodeContent(ToStringBuilder.toString(aggregateNode).toUpperCase());
		
		ValueNode operandNode = aggregateNode.getOperand();
		
		if (operandNode instanceof ColumnReference) {
			ColumnReference columnRef = (ColumnReference) operandNode;
			tableColumn.setName(columnRef.getColumnName());
		} else if (operandNode instanceof ConstantNode) {
			ConstantNode constantNode = (ConstantNode) operandNode;
			tableColumn.setValue(constantNode.getValue());
		}
		if (tableColumn.getAliasName() == null
				&& aggregateNode.getColumnName() != null) {
			tableColumn.setAliasName(aggregateNode.getColumnName());
		} else {
			if (tableColumn.getAliasName() == null) {
				tableColumn.setAliasName(aggregateNode.getAggregateName());
			}
		}
	}

	private static void initColumn(TableColumn column, ColumnReference reference) {
		if (column.getTable() == null)
			column.setTable(reference.getTableName());
		if (column.getName() == null) {
			column.setName(reference.getColumnName());
		}
		if (column.getSchema() == null) {
			column.setSchema(reference.getSchemaName());
		}
		if (column.getTable() != null) {
			column.setTable(reference.getTableName());
		}
		if (column.getAliasName() == null) {
			column.setAliasName(reference.getColumnName());
		}
	}

}
