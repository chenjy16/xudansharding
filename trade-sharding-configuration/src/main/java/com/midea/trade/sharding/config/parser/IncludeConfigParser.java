package com.midea.trade.sharding.config.parser;
import org.w3c.dom.Element;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.IncludeConfig;



/**
 * Include配置解析器
 */
public class IncludeConfigParser implements Parser<IncludeConfig> {

	@Override
	public IncludeConfig parse(Element el) {
		IncludeConfig includeConfig = new IncludeConfig();
		String filePath = ParseUtils.getAttr(el, "file");
		includeConfig.setFile(filePath);
		Configurations.registerConfig(includeConfig);
		return includeConfig;
	}

}
