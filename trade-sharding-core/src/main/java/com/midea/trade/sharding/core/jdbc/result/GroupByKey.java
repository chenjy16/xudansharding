
package com.midea.trade.sharding.core.jdbc.result;

import java.sql.SQLException;

/**
 * group by 关键字约束
 */
public interface GroupByKey {
	
	public Object key() throws SQLException;

}
