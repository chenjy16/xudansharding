package com.midea.trade.sharding.config.parser;
import org.w3c.dom.Element;

import com.midea.trade.sharding.config.BeanConfig;
import com.midea.trade.sharding.config.Configurations;

/**
 * Bean配置解析器
 * 
 */
public class BeanConfigParser implements Parser<BeanConfig> {

	@Override
	public BeanConfig parse(Element el) {
		BeanConfig beanConfig = new BeanConfig();
		String id = ParseUtils.getAttr(el, ID_ATTR);
		beanConfig.setId(id);
		String className = ParseUtils.getAttr(el, "class");
		beanConfig.setClassName(className);
		Configurations.registerConfig(beanConfig);
		return beanConfig;
	}

}
