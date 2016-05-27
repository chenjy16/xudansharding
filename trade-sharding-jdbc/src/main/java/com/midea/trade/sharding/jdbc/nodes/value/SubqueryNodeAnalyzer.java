package com.midea.trade.sharding.jdbc.nodes.value;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.SubqueryNode;
import com.foundationdb.sql.parser.ValueNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;

/**
 * SQL节点解析器:SubqueryNodeAnalyzer
 * 
 */
public class SubqueryNodeAnalyzer extends
		AbstractNodeAnalyzer<SubqueryNode, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.SUBQUERY_NODE};

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	/**
	 * 将in查询解析分解成N个，根据这n个值进行路由，假如存在子查询，则继续解析
	 */
	@Override
	public AnalyzeResult doAnalyze(SubqueryNode valueNode) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult(); 
		this.analyzeAndMergeResult(result, valueNode.getResultSet());
		return result;
	}

	public static void initColumnValue(TableColumn column, ValueNode valueNode) {
		column.setName(valueNode.getColumnName());
		column.setTable(valueNode.getTableName());
		try {
			column.setSchema(valueNode.getSchemaName());
		} catch (StandardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
