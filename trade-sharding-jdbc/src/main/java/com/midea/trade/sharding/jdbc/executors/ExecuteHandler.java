package com.midea.trade.sharding.jdbc.executors;
import java.sql.SQLException;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.shard.RouteTarget;




/**
 * 执行操作约束
 */
public interface ExecuteHandler<T> {

	public T handle(RouteTarget target, StatementContext context,
			ExecuteCallback<T> callback) throws SQLException;

}
