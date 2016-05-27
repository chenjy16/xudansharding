package com.midea.trade.sharding.jdbc.nodes.resultset;

import com.foundationdb.sql.parser.FromBaseTable;
import com.foundationdb.sql.parser.JoinNode;
import com.foundationdb.sql.parser.NodeTypes;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.TableInfo;
import com.midea.trade.sharding.jdbc.nodes.AbstractNodeAnalyzer;
import com.midea.trade.sharding.jdbc.nodes.DefaultAnalyzeResult;



/**
 * SQL节点解析器:JoinNodeAnalyzer
 * 
 */
public class JoinNodeAnalyzer extends
		AbstractNodeAnalyzer<JoinNode, AnalyzeResult> {
	
	int[] nodeTypes = { NodeTypes.JOIN_NODE, NodeTypes.HALF_OUTER_JOIN_NODE };

	@Override
	public int[] getNodeTypes() {
		return nodeTypes;
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

	@Override
	public AnalyzeResult doAnalyze(JoinNode joinNode) {
		DefaultAnalyzeResult result = new DefaultAnalyzeResult();
		if (joinNode.getLeftResultSet().getNodeType() == NodeTypes.FROM_BASE_TABLE) {
			TableInfo tableInfo=this.createTableInfo((FromBaseTable) joinNode.getLeftResultSet());
			result.getTableInfos().add(tableInfo); 
		} else {
			analyzeAndMergeResult(result, joinNode.getLeftResultSet());
		}
		if(joinNode.getRightResultSet().getNodeType()== NodeTypes.FROM_BASE_TABLE){
			TableInfo tableInfo=this.createTableInfo((FromBaseTable) joinNode.getRightResultSet());
			result.getTableInfos().add(tableInfo); 
		} else {
			analyzeAndMergeResult(result, joinNode.getRightResultSet());
		}
		
		analyzeAndMergeResult(result, joinNode.getJoinClause());
		return result;
	}

}
