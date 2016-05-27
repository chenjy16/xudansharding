package com.midea.trade.sharding.core.cache;
import java.io.Serializable;

/**
 * 内部缓存
 */
public interface Cache<K extends Serializable, V extends Serializable> {

	public void put(K key, V value);

	public V remove(K key);

	public V get(K key);

}
