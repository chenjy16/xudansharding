package com.midea.trade.sharding.druid.visitor;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.wall.spi.WallVisitorUtils;


/**
 * 从ServerRouterUtil中抽取的一些公用方法，路由解析工具类
 */
public class RouterUtil {
	
	/**
	 * 移除执行语句中的数据库名
	 * @param stmt		 执行语句
	 * @param schema  	数据库名
	 * @return 			执行语句
	 * @author mycat
	 */

	public static String removeSchema(String stmt, String schema) {
		final String upStmt = stmt.toUpperCase();
		final String upSchema = schema.toUpperCase() + ".";
		int strtPos = 0;
		int indx = 0;
		boolean flag = false;
		indx = upStmt.indexOf(upSchema, strtPos);
		if (indx < 0) {
			StringBuilder sb = new StringBuilder("`").append(
					schema.toUpperCase()).append("`.");
			indx = upStmt.indexOf(sb.toString(), strtPos);
			flag = true;
			if (indx < 0) {
				return stmt;
			}
		}
		StringBuilder sb = new StringBuilder();
		while (indx > 0) {
			sb.append(stmt.substring(strtPos, indx));
			strtPos = indx + upSchema.length();
			if (flag) {
				strtPos += 2;
			}
			indx = upStmt.indexOf(upSchema, strtPos);
		}
		sb.append(stmt.substring(strtPos));
		return sb.toString();
	}

	




	/**
	 * 判断条件是否永真
	 * @param expr
	 * @return
	 */
	public static boolean isConditionAlwaysTrue(SQLExpr expr) {
		Object o = WallVisitorUtils.getValue(expr);
		if(Boolean.TRUE.equals(o)) {
			return true;
		}
		return false;
	}

	/**
	 * 判断条件是否永假的
	 * @param expr
	 * @return
	 */
	public static boolean isConditionAlwaysFalse(SQLExpr expr) {
		Object o = WallVisitorUtils.getValue(expr);
		if(Boolean.FALSE.equals(o)) {
			return true;
		}
		return false;
	}



	/**
	 * 是否为批量插入：insert into ...values (),()...或 insert into ...select.....
	 *
	 * @param insertStmt
	 * @return
	 */
	private static boolean isMultiInsert(MySqlInsertStatement insertStmt) {
		return (insertStmt.getValuesList() != null && insertStmt.getValuesList().size() > 1)
				|| insertStmt.getQuery() != null;
	}

}