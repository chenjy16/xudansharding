package com.midea.trade.sharding.client.idgenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 序列化ID生产者
 */
public class SequenceValueProducer implements ValueProducer<Long> {
	private final AtomicLong sequence;
	private int step;

	public SequenceValueProducer(long initValue, int step) {
		sequence = new AtomicLong(initValue);
		this.step = step;
	}

	@Override
	public Long produce() {
		Long value = sequence.addAndGet(step);
		return value;
	}
	
	public static void main(String args[]){
		
		for(int i=0;i<10000000;i++){
			System.out.println("i="+i+" pos="+(i&63));
		}
	}

}
