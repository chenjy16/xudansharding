package com.midea.trade.sharding.jdbc.router;

import java.util.List;

/**
 * KeyValue
 */
class KeyValue {
	
	KeyValue parent;
	
	String key;
	
	Object value;
	
	List<KeyValue> childs;
	
}
