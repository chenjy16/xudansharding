package com.midea.trade.sharding.druid.parser.impl;
import java.sql.SQLNonTransientException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.stat.TableStat.Condition;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.druid.node.DruidShardingParseInfo;
import com.midea.trade.sharding.druid.node.RangeValue;
import com.midea.trade.sharding.druid.node.RouteCalculateUnit;
import com.midea.trade.sharding.druid.parser.DruidParser;
import com.midea.trade.sharding.druid.util.StringUtil;
import com.midea.trade.sharding.druid.visitor.DruidSchemaStatVisitor;

/**
 * 对SQLStatement解析
 * 主要通过visitor解析和statement解析：有些类型的SQLStatement通过visitor解析足够了，
 * 有些只能通过statement解析才能得到所有信息
 * 有些需要通过两种方式解析才能得到完整信息
 */
public class DefaultDruidParser implements  DruidParser{
	/**
	 * 解析得到的结果
	 */
	protected DruidShardingParseInfo ctx;
	
	private Map<String,String> tableAliasMap = new HashMap<String,String>();

	private List<Condition> conditions = new ArrayList<Condition>();
	
	public Map<String, String> getTableAliasMap() {
		return tableAliasMap;
	}

	public List<Condition> getConditions() {
		return conditions;
	}


	@Override
	public void parser(Configurations configurations,
			SQLStatement stmt, DruidSchemaStatVisitor schemaStatVisitor)
			throws SQLNonTransientException {
		         ctx = new DruidShardingParseInfo();
				//设置为原始sql，如果有需要改写sql的，可以通过修改SQLStatement中的属性，然后调用SQLStatement.toString()得到改写的sql
				//通过visitor解析
				 visitorParse(stmt,schemaStatVisitor);
				//通过Statement解析
				statementParse(configurations, stmt);
	}

	@Override
	public void statementParse(Configurations configurations, SQLStatement stmt)
			throws SQLNonTransientException {
		
	}

	
	
	@Override
	public void visitorParse(SQLStatement stmt,
			DruidSchemaStatVisitor visitor) throws SQLNonTransientException {
		
		stmt.accept(visitor);
		List<List<Condition>> mergedConditionList = new ArrayList<List<Condition>>();
		if(visitor.hasOrCondition()) {//包含or语句
			//TODO
			//根据or拆分
			mergedConditionList = visitor.splitConditions();
		} else {//不包含OR语句
			mergedConditionList.add(visitor.getConditions());
		}
		
		//表的别名
		if(visitor.getAliasMap() != null) {
			for(Map.Entry<String, String> entry : visitor.getAliasMap().entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				if(key != null && key.indexOf("`") >= 0) {
					key = key.replaceAll("`", "");
				}
				if(value != null && value.indexOf("`") >= 0) {
					value = value.replaceAll("`", "");
				}
				//表名前面带database的，去掉
				if(key != null) {
					int pos = key.indexOf(".");
					if(pos> 0) {
						key = key.substring(pos + 1);
					}
				}
				
				if(key.equals(value)) {
					ctx.addTable(key.toUpperCase());
				} 
				tableAliasMap.put(key.toUpperCase(), value);
			}
			visitor.getAliasMap().putAll(tableAliasMap);
			ctx.setTableAliasMap(tableAliasMap);
		}
		ctx.setRouteCalculateUnits(this.buildRouteCalculateUnits(visitor, mergedConditionList));
	}
	
	
	/**
	* @author chejy 
	* @Description: 
	* @param visitor
	* @param conditionList
	* @return List<RouteCalculateUnit>    返回类型
	* @throws
	 */
	private List<RouteCalculateUnit> buildRouteCalculateUnits(DruidSchemaStatVisitor visitor, List<List<Condition>> conditionList) {
		List<RouteCalculateUnit> retList = new ArrayList<RouteCalculateUnit>();
		//遍历condition ，找分片字段
		for(int i = 0; i < conditionList.size(); i++) {
			RouteCalculateUnit routeCalculateUnit = new RouteCalculateUnit();
			for(Condition condition : conditionList.get(i)) {
				List<Object> values = condition.getValues();
				if(values.size() == 0) {
					break;
				}
				if(checkConditionValues(values)) {
					String columnName = StringUtil.removeBackquote(condition.getColumn().getName().toUpperCase());
					String tableName = StringUtil.removeBackquote(condition.getColumn().getTable().toUpperCase());
					
					if(visitor.getAliasMap() != null && visitor.getAliasMap().get(tableName) != null 
							&& !visitor.getAliasMap().get(tableName).equals(tableName)) {
						tableName = visitor.getAliasMap().get(tableName);
					}
					
					if(visitor.getAliasMap() != null && visitor.getAliasMap().get(condition.getColumn().getTable().toUpperCase()) == null) {//子查询的别名条件忽略掉,不参数路由计算，否则后面找不到表
						continue;
					}
					
					String operator = condition.getOperator();
					
					//只处理between ,in和=3中操作符
					if(operator.equals("between")) {
						RangeValue rv = new RangeValue(values.get(0), values.get(1), RangeValue.EE);
						
								routeCalculateUnit.addShardingExpr(tableName.toUpperCase(), columnName, rv);
								
					} else if(operator.equals("=") || operator.toLowerCase().equals("in")){ //只处理=号和in操作符,其他忽略
						
								routeCalculateUnit.addShardingExpr(tableName.toUpperCase(), columnName, values.toArray());
								
					}
				}
			}
			retList.add(routeCalculateUnit);
		}
		return retList;
	}
	
	
	private boolean checkConditionValues(List<Object> values) {
		for(Object value : values) {
			if(value != null && !value.toString().equals("")) {
				return true;
			}
		}
		return false;
	}
	

	@Override
	public DruidShardingParseInfo getCtx() {
		return ctx;
	}
}
