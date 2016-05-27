package com.midea.trade.sharding.jdbc.nodes.list;

import java.util.Iterator;

import com.foundationdb.sql.parser.FromBaseTable;
import com.foundationdb.sql.parser.FromList;
import com.foundationdb.sql.parser.FromTable;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableInfo;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;

/**
 * SQL节点解析器:FromListNodeAnalyzer
 * 
 */
public class FromListNodeAnalyzer extends
		AbstractNodeAnalyzer<FromList, AnalyzeResult> {
	int[] nodeTypes = { NodeTypes.FROM_LIST };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
	}

	@Override
	public AnalyzeResult doAnalyze(FromList fromList) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		Iterator<FromTable> iterator = fromList.iterator();
		while (iterator.hasNext()) {
			FromTable fromTable = iterator.next();
			if (fromTable.getNodeType() != NodeTypes.FROM_BASE_TABLE) {
				this.analyzeAndMergeResult(result, fromTable);
				continue;
			}
			TableInfo info = createTableInfo((FromBaseTable) fromTable);
			result.getTableInfos().add(info);
		}

		return result;
	}

	TableInfo createTableInfo(FromBaseTable fromTable) {
		TableInfo info = new TableInfo();
		try {
			info.setSchema(fromTable.getOrigTableName().getSchemaName());
			info.setOrgName(fromTable.getOrigTableName().getTableName());
			info.setName(fromTable.getTableName().getTableName());
			info.setTableNode(fromTable.getOrigTableName());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return info;
	}
}
