package com.midea.trade.sharding.client.idgenerator;

import java.util.concurrent.BlockingQueue;

/**
 * 序列化ID生成器任务
 */
public class SequenceGeneratorTask implements Runnable {
	
	private final BlockingQueue<Long> valuesQueue;
	final ValueProducer<Long> producer;

	public SequenceGeneratorTask(BlockingQueue<Long> queue,
			ValueProducer<Long> producer) {
		this.valuesQueue = queue;
		this.producer = producer;
	}

	@Override
	public void run() {
		while (true) {
			try {
				Long value = producer.produce();
				valuesQueue.put(value);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
