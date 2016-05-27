package com.midea.trade.sharding.config.factory;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.midea.trade.sharding.config.ThreadPoolConfig;
import com.midea.trade.sharding.core.factory.ObjectFactory;



/**
 * 连接池配置工厂
 * 
 */
public class ThreadPoolFactory implements ObjectFactory<ThreadPoolConfig> {

	@Override
	public Object create(final ThreadPoolConfig config) {
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(config.getCoreSize(), 
				config.getCoreSize(), config.getTimeout(), TimeUnit.MILLISECONDS, 
				new LinkedBlockingQueue<Runnable>(config.getQueueSize()),
				new ThreadFactory() {
					@Override
					public Thread newThread(Runnable r) {
						Thread t = new Thread(r);
						t.setName("ShardingClient-thread-"+config.getId());
						return t;
					}
				},
				
				new RejectedExecutionHandler() {
					@Override
					public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
						throw new RejectedExecutionException("ShardingClient-thread-" + config.getId() + 
                                r.toString() + " rejected from " + executor.toString());
					}
				});
		
		if(config.getTimeout() > 1L){
			threadPool.allowCoreThreadTimeOut(true);
		}
		return threadPool;
	}

}
