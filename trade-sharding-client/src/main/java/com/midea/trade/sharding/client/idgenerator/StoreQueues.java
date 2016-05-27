package com.midea.trade.sharding.client.idgenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;

@SuppressWarnings("unchecked")
public class StoreQueues {
	
	@SuppressWarnings("rawtypes")
	static final Map<String, BlockingQueue> blockingQueues = new HashMap<String, BlockingQueue>();
	static Executor executor = Executors
			.newCachedThreadPool(new ThreadFactory() {

				@Override
				public Thread newThread(Runnable r) {
					Thread t = new Thread(r);
					t.setName("id generator thread");
					t.setDaemon(true);
					return t;
				}
			});

	public static <T> BlockingQueue<T> getQueue(String name) {
		return blockingQueues.get(name);
	}

	public static synchronized void createQueue(Config config) {

		switch (config.type) {
		case SEQUENCE:
			BlockingQueue<Long> blockingQueue = new LinkedBlockingQueue<Long>(
					config.concurrentSize);
			blockingQueues.put(config.name, blockingQueue);
			ValueProducer<Long> producer = new SequenceValueProducer(
					config.initValue, config.step);
			executor.execute(new SequenceGeneratorTask(blockingQueue, producer));
			break;
		default:
			break;
		}
	}

}
