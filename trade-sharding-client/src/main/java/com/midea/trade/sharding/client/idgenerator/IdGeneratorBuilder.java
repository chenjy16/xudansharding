package com.midea.trade.sharding.client.idgenerator;

/**
 * ID 生成器构建器
 */
public abstract class IdGeneratorBuilder {

	public static <T> IdGenerator<Long> build(Config config) {
		StoreQueues.createQueue(config);
		IdGenerator<Long> generator = new QueuedSequenceIdGenerator(config.name);
		return generator;
	}

	public static void main(String args[]) {
		Config config = new Config();
		final IdGenerator<Long> sequence = IdGeneratorBuilder.build(config);
		for (int i = 0; i < 5; i++) {
			new Thread() {
				public void run() {
					while (true)
						System.out.println(Thread.currentThread() + "--"
								+ sequence.generate());
				}
			}.start();
		}

	}
}
