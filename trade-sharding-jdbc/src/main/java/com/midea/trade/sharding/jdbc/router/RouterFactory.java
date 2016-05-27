package com.midea.trade.sharding.jdbc.router;
import com.midea.trade.sharding.core.context.StatementContext;



/**
 * 路由工厂
 */
public class RouterFactory {
	
	static SimpleRouter router=new SimpleRouter();
	
	public static Router createRouter(StatementContext context){
		return router;
	}

}
