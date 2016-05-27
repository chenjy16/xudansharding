package com.midea.trade.sharding.druid.node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.midea.trade.sharding.druid.route.HavingCols;
import com.midea.trade.sharding.druid.route.SQLMerge;
import com.midea.trade.sharding.druid.visitor.WhereUnit;


public class DruidShardingParseInfo {
	/**
	 * 一个sql中可能有多个WhereUnit（如子查询中的where可能导致多个）
	 */
	private List<WhereUnit> whereUnits = new ArrayList<WhereUnit>();
	private List<RouteCalculateUnit> routeCalculateUnits = new ArrayList<RouteCalculateUnit>();
	/**
	 * （共享属性）
	 */
	private String sql = "";
	
	//tables为路由计算共享属性，多组RouteCalculateUnit使用同样的tables
	private List<String> tables = new ArrayList<String>();
	/**
	 * key table alias, value talbe realname;
	 */
	private Map<String, String> tableAliasMap = new LinkedHashMap<String, String>();
	
	SQLMerge sqlMerge;
	
	

	public Map<String, String> getTableAliasMap() {
		return tableAliasMap;
	}

	public void setTableAliasMap(Map<String, String> tableAliasMap) {
		this.tableAliasMap = tableAliasMap;
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public List<String> getTables() {
		return tables;
	}

	public void addTable(String tableName) {
		this.tables.add(tableName);
	}

	public RouteCalculateUnit getRouteCalculateUnit() {
		return routeCalculateUnits.get(0);
	}
	
	public List<RouteCalculateUnit> getRouteCalculateUnits() {
		return routeCalculateUnits;
	}
	
	public void setRouteCalculateUnits(List<RouteCalculateUnit> routeCalculateUnits) {
		this.routeCalculateUnits = routeCalculateUnits;
	}
	
	public void addRouteCalculateUnit(RouteCalculateUnit routeCalculateUnit) {
		this.routeCalculateUnits.add(routeCalculateUnit);
	}
	

	public void clear() {
		for(RouteCalculateUnit unit : routeCalculateUnits ) {
			unit.clear();
		}
	}

	
	

	//=========================================排序和聚合=====================================================
    public void setOrderByCols(LinkedHashMap<String, Integer> orderByCols) {
        if (orderByCols != null && !orderByCols.isEmpty()) {
            createSQLMergeIfNull().setOrderByCols(orderByCols);
        }
    }

    public void setHasAggrColumn(boolean hasAggrColumn) {
        if (hasAggrColumn) {
            createSQLMergeIfNull().setHasAggrColumn(true);
        }
    }

    public void setGroupByCols(String[] groupByCols) {
        if (groupByCols != null && groupByCols.length > 0) {
            createSQLMergeIfNull().setGroupByCols(groupByCols);
        }
    }

    public void setMergeCols(Map<String, Integer> mergeCols) {
        if (mergeCols != null && !mergeCols.isEmpty()) {
            createSQLMergeIfNull().setMergeCols(mergeCols);
        }

    }

    public LinkedHashMap<String, Integer> getOrderByCols() {
        return (sqlMerge != null) ? sqlMerge.getOrderByCols() : null;

    }
    
    private SQLMerge createSQLMergeIfNull() {
        if (sqlMerge == null) {
            sqlMerge = new SQLMerge();
        }
        return sqlMerge;
    }
    
    public void setHavings(HavingCols havings) {
		if (havings != null) {
			createSQLMergeIfNull().setHavingCols(havings);
		}
	}
}
