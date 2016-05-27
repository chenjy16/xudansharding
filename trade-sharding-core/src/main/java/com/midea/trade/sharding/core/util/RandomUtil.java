package com.midea.trade.sharding.core.util;

import java.util.Random;
import java.util.UUID;

/**
 * RandomUtil
 */
public class RandomUtil {

	public static final Random random = new Random();
	
	public static int nextInt() {
		return random.nextInt() & 2147483647 | 16777472;
	}

	public static int nextInt(int max) {
		return random.nextInt(max);
	}

	public static int nextInt(int min, int max) {
		if(min == max){
			return min;
		}
		return random.nextInt((max - min) + 1) + min;
	}
	
	public static long nextLong(long min, long max) {
		return (long) nextDouble(min, max);
	}
	
	public static double nextDouble(double min, double max) {
        if (min == max) {
            return min;
        }
        return ((max - min) * random.nextDouble()) + min;
    }
	
	public static String uuid(){
		return UUID.randomUUID().toString();
	}
	
}
