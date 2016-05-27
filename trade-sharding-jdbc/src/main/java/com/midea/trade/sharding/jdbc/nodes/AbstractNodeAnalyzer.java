package com.midea.trade.sharding.jdbc.nodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foundationdb.sql.parser.ConstantNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.QueryTreeNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;



/**
 * SQL节点解析器基类
 * 
 */
public abstract class AbstractNodeAnalyzer<N extends QueryTreeNode, R extends AnalyzeResult>
		implements NodeAnalyzer<N, R> {
	
	protected static Logger logger = LoggerFactory
			.getLogger(AbstractNodeAnalyzer.class);

	@Override
	public R analyze(N node) {
		for (int nodeType : getNodeTypes()) {
			if (node.getNodeType() == nodeType) {
				return doAnalyze(node);
			}
		}

		throw new IllegalArgumentException("node type not match!node=" + node);
	}

	public abstract R doAnalyze(N node);

	protected void analyzeAndMergeResult(DefaultAnalyzeResult result,
			QueryTreeNode node) {
		if (node != null) {
			if (node instanceof ConstantNode|| node.getNodeType() == NodeTypes.PARAMETER_NODE
					|| node.getNodeType() == NodeTypes.COLUMN_REFERENCE) {
				return;
			}
			NodeAnalyzer<QueryTreeNode, AnalyzeResult> analyzer = Analyzers.get(node);
			
			if (analyzer == null) {
				logger.error("analyzer is null!node=" + node);
				return;
			}
			AnalyzeResult fromListResult = analyzer.analyze(node);
			if (fromListResult == null) {
				return;
			}
			result.addTables(fromListResult.getTableInfos());
			result.addConditionColumns(fromListResult.getConditionColumns());
			result.getAggregateColumns().addAll(fromListResult.getAggregateColumns()); 
		}
	}

}
