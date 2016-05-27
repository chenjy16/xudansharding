package com.midea.trade.sharding.jdbc.check;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.shard.AnalyzeResult;


/**
 * 语句检查约束
 */
public interface Checker {

	boolean check(AnalyzeResult result) throws ShardException;

}
