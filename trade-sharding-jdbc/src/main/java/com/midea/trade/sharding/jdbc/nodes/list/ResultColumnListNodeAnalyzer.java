package com.midea.trade.sharding.jdbc.nodes.list;

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.foundationdb.sql.parser.NodeTypes;
import com.foundationdb.sql.parser.ResultColumn;
import com.foundationdb.sql.parser.ResultColumnList;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;
import com.midea.trade.sharding.jdbc.nodes.NodeHelper;


/**
 * SQL节点解析器:ResultColumnListNodeAnalyzer
 * 
 */
public class ResultColumnListNodeAnalyzer extends
		AbstractNodeAnalyzer<ResultColumnList, AnalyzeResult> {
	static final Logger logger = LoggerFactory
			.getLogger(ResultColumnListNodeAnalyzer.class);
	static final int[] nodeTypes = { NodeTypes.RESULT_COLUMN_LIST };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(ResultColumnList node) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		Iterator<ResultColumn> resultColumnsIterator = node.iterator();
		while (resultColumnsIterator.hasNext()) {
			ResultColumn resultColumn = resultColumnsIterator.next();
			TableColumn tableColumn = NodeHelper
					.parseResultColumn(resultColumn);
			result.getResultColumns().add(tableColumn);

		}
		return result;
	}

}
