package com.midea.trade.sharding.jdbc.dispatcher;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import com.midea.trade.sharding.core.context.ConnectionContext;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.jdbc.ConnectionWrapper;
import com.midea.trade.sharding.jdbc.builder.DefaultStatementContextBuilder;
import com.midea.trade.sharding.jdbc.builder.StatementContextBuilder;
import com.midea.trade.sharding.jdbc.executors.Executor;
import com.midea.trade.sharding.jdbc.router.Router;
import com.midea.trade.sharding.jdbc.router.RouterFactory;


public class DispatcherTest {
	
	@Test
	public void dispathcer() throws SQLException{
		String sql = "select * from usertable u, infotable i where u.uid=i.uid and u.uid=?";
		String sql2 = "select * from usertable u limit 10,10";
		ConnectionContext connectionContext = new ConnectionContext(new ConnectionWrapper(null), null);
		ConnectionContext.setContext(connectionContext);
		StatementContextBuilder builder = new DefaultStatementContextBuilder();
		//context里面设置 sql节点树和sql解析结果
		//where 后面条件列的值设置
		StatementContext context = builder.build(sql,StatementContext.getContext());
		Router router = RouterFactory.createRouter(context);
		// TODO event && parameters
		Executor<ResultSet> executor = (Executor<ResultSet>) router.route(context);
	}

}

