package com.midea.trade.sharding.config.parser;
import org.w3c.dom.Element;
import com.midea.trade.sharding.config.Config;



/**
 * 配置解析器约束
 * 
 */
public interface Parser<T extends Config> {
	
	String ID_ATTR = "id";
	String TEMPLAT_ATTR = "template";
	String LOADBLANCE_ATTR = "loadbalance";

	T parse(Element el);
	
}
