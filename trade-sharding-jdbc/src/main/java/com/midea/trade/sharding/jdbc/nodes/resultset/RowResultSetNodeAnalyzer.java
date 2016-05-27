package com.midea.trade.sharding.jdbc.nodes.resultset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.foundationdb.sql.parser.ConstantNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.foundationdb.sql.parser.RowResultSetNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.Analyzers;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.NodeAnalyzer;

/**
 * SQL节点解析器:RowResultSetNodeAnalyzer
 * 
 */
public class RowResultSetNodeAnalyzer extends
		AbstractNodeAnalyzer<RowResultSetNode, AnalyzeResult> {
	static Logger logger = LoggerFactory
			.getLogger(RowResultSetNodeAnalyzer.class);
	int[] nodeTypes = { NodeTypes.ROW_RESULT_SET_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(RowResultSetNode node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		this.analyzeAndMergeResult(result, node.getResultColumns());
		return result;
	}

	protected void analyzeAndMergeResult(DefaultAnalyzeResult result,
			QueryTreeNode node) {
		if (node != null) {
			if (node instanceof ConstantNode
					|| node.getNodeType() == NodeTypes.PARAMETER_NODE
					|| node.getNodeType() == NodeTypes.COLUMN_REFERENCE) {
				return;
			}
			NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers
					.get(node.getNodeType());
			if (analyzer == null) {
				if (logger.isWarnEnabled()) {
					logger.warn("analyzer is null,node=" + node);
				}
				return;
			}
			AnalyzeResult fromListResult = analyzer.analyze(node);
			if (fromListResult == null) {
				return;
			}
			result.addTables(fromListResult.getTableInfos());
			result.addConditionColumns(fromListResult.getConditionColumns());
			result.addResultColumns(fromListResult.getResultColumns()); 
		}
	}
}
