package com.midea.trade.sharding.jdbc.nodes;

import com.foundationdb.sql.parser.QueryTreeNode;
import com.midea.trade.sharding.core.shard.AnalyzeResult;


/**
 * 节点解析器约束
 */
public interface NodeAnalyzer<N extends QueryTreeNode, R extends AnalyzeResult> {

	R analyze(N node);

	int[] getNodeTypes();

}
