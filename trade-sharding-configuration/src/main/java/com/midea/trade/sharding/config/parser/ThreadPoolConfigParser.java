package com.midea.trade.sharding.config.parser;
import org.w3c.dom.Element;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.config.ThreadPoolConfig;


/**
 * 连接池配置解析器
 */
public class ThreadPoolConfigParser implements Parser<ThreadPoolConfig> {
	static final int CORE_SIZE = 50;

	@Override
	public ThreadPoolConfig parse(Element el) {
		ThreadPoolConfig threadPoolConfig = new ThreadPoolConfig();
		String id = ParseUtils.getAttr(el, ID_ATTR);
		threadPoolConfig.setId(id);
		String sizeStr = ParseUtils.getAttr(el, "size");
		Integer coreSize = ((sizeStr == null || sizeStr.trim().length() <= 0) ? 
				CORE_SIZE : Integer.valueOf(sizeStr));
		threadPoolConfig.setCoreSize(coreSize); 
		
		String queueSizeStr = ParseUtils.getAttr(el, "qsize");
		if(queueSizeStr!=null){
			threadPoolConfig.setQueueSize(Integer.valueOf(queueSizeStr));
		}
		
		String timeoutStr = ParseUtils.getAttr(el, "timeout");
		if(queueSizeStr!=null){
			threadPoolConfig.setTimeout(Long.valueOf(timeoutStr));
		}
		
		Configurations.registerConfig(threadPoolConfig);
		return threadPoolConfig;
	}

}
