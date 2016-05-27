package com.midea.trade.sharding.client.idgenerator;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 序列化ID生成器
 */
public class QueuedSequenceIdGenerator implements IdGenerator<Long> {
	String name;

	final BlockingQueue<Long> queue;

	public QueuedSequenceIdGenerator(String name) {
		this(name, 100000);
	}

	public QueuedSequenceIdGenerator(String name, int size) {
		this.name = name;
		queue = StoreQueues.getQueue(name);
		assert queue != null;
	}

	@Override
	public Long generate() {
		try {
			return queue.poll(1, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			throw new IdGenerateException("generate exception!thread="
					+ Thread.currentThread(), e);
		}
	}

}
