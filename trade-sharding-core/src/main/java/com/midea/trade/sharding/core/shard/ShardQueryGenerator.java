package com.midea.trade.sharding.core.shard;

import com.midea.trade.sharding.core.resources.NameNodeHolder;



public interface ShardQueryGenerator {
	
	int WITH_TABLE = 1;
	int LIMIT_AVG = 2;

	String generate(NameNodeHolder nameNode, AnalyzeResult analyzeResult); 

}
