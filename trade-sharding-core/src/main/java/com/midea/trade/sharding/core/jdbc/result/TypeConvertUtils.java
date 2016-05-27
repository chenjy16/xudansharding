package com.midea.trade.sharding.core.jdbc.result;

import java.sql.SQLException;
import org.apache.commons.beanutils.ConvertUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.jdbc.aggregate.Aggregate;
 

/**
 * 类型转换工具
 */
public abstract class TypeConvertUtils {
	static Logger logger=LoggerFactory.getLogger(TypeConvertUtils.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <T> T convert(Object value, Class<T> claz) {
		if(value instanceof Aggregate){
			try {
				value=((Aggregate)value).value(); 
			} catch (SQLException e) {
				logger.error("Aggregate error!",e);
				throw new ShardException("shard error",e);
			}
		}
		return (T) ConvertUtils.convert(value, claz);
	}

}
