package com.midea.trade.sharding.druid.parser.impl;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLName;
import com.alibaba.druid.sql.ast.SQLOrderingSpecification;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLAggregateExpr;
import com.alibaba.druid.sql.ast.expr.SQLAllColumnExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLMethodInvokeExpr;
import com.alibaba.druid.sql.ast.expr.SQLNumericLiteralExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLTextLiteralExpr;
import com.alibaba.druid.sql.ast.statement.SQLSelectGroupByClause;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQuery;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlSelectGroupByExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUnionQuery;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.druid.node.MergeCol;
import com.midea.trade.sharding.druid.route.HavingCols;
import com.midea.trade.sharding.druid.route.OrderCol;
import com.midea.trade.sharding.druid.util.ObjectUtil;
import com.midea.trade.sharding.druid.util.StringUtil;



public class DruidSelectParser extends DefaultDruidParser {
    protected boolean isNeedParseOrderAgg=true;
	
    @Override
	public void statementParse(Configurations configurations,SQLStatement stmt) {
    	
		SQLSelectStatement selectStmt = (SQLSelectStatement)stmt;
		SQLSelectQuery sqlSelectQuery = selectStmt.getSelect().getQuery();
		if(sqlSelectQuery instanceof MySqlSelectQueryBlock) {
			
			MySqlSelectQueryBlock mysqlSelectQuery = (MySqlSelectQueryBlock)selectStmt.getSelect().getQuery();
				 parseOrderAggGroupMysql(configurations, stmt, mysqlSelectQuery);
		} else if (sqlSelectQuery instanceof MySqlUnionQuery) { 

		}
	}
    
    
	protected void parseOrderAggGroupMysql(Configurations configurations, 
			SQLStatement stmt, MySqlSelectQueryBlock mysqlSelectQuery){
		  	if(!isNeedParseOrderAgg){
	            return;
	        }
			Map<String, String> aliaColumns = parseAggGroupCommon(configurations, stmt, mysqlSelectQuery);
			//setOrderByCols
			if(mysqlSelectQuery.getOrderBy() != null) {
				List<SQLSelectOrderByItem> orderByItems = mysqlSelectQuery.getOrderBy().getItems();
				ctx.setOrderByCols(buildOrderByCols(orderByItems,aliaColumns));
			}
	        isNeedParseOrderAgg=false;
	}
	
	
	private LinkedHashMap<String, Integer> buildOrderByCols(
			List<SQLSelectOrderByItem> orderByItems,
			Map<String, String> aliaColumns) {
		LinkedHashMap<String, Integer> map = new LinkedHashMap<String, Integer>();
		for(int i= 0; i < orderByItems.size(); i++) {
			
			SQLOrderingSpecification type = orderByItems.get(i).getType();
            //orderColumn只记录字段名称,因为返回的结果集是不带表名的。
			SQLExpr expr =  orderByItems.get(i).getExpr();
			String col;
			if (expr instanceof SQLName) {
			   col = ((SQLName)expr).getSimpleName();
			}
			else {
				col =expr.toString();
			}
			if(type == null) {
				type = SQLOrderingSpecification.ASC;
			}
			col=getAliaColumn(aliaColumns,col);//此步骤得到的col必须是不带.的，有别名的用别名，无别名的用字段名
			map.put(col, type == SQLOrderingSpecification.ASC ? OrderCol.COL_ORDER_TYPE_ASC : OrderCol.COL_ORDER_TYPE_DESC);
		
		}
		return map;
	}

	protected Map<String, String> parseAggGroupCommon(Configurations configurations, SQLStatement stmt, SQLSelectQueryBlock mysqlSelectQuery){
		Map<String, String> aliaColumns = new HashMap<String, String>();
		Map<String, Integer> aggrColumns = new HashMap<String, Integer>();
		List<SQLSelectItem> selectList = mysqlSelectQuery.getSelectList();
        boolean isNeedChangeSql=false;
        int size = selectList.size();
        boolean isDistinct=mysqlSelectQuery.getDistionOption()==2;
        for (int i = 0; i < size; i++){
			SQLSelectItem item = selectList.get(i);
			//聚合列
			if (item.getExpr() instanceof SQLAggregateExpr){
				SQLAggregateExpr expr = (SQLAggregateExpr) item.getExpr();
				String method = expr.getMethodName();
				//只处理有别名的情况，无别名添加别名，否则某些数据库会得不到正确结果处理
				int mergeType = MergeCol.getMergeType(method);
                if (MergeCol.MERGE_AVG == mergeType){
                	
                	
                	//跨分片avg需要特殊处理，直接avg结果是不对的
                    String colName = item.getAlias() != null ? item.getAlias() : method + i;
                    SQLSelectItem sum =new SQLSelectItem();
                    String sumColName = colName + "SUM";
                    sum.setAlias(sumColName);
                    SQLAggregateExpr sumExp =new SQLAggregateExpr("SUM");
                    ObjectUtil.copyProperties(expr,sumExp);
                    sumExp.getArguments().addAll(expr.getArguments());
                    sumExp.setMethodName("SUM");
                    sum.setExpr(sumExp);
                    selectList.set(i, sum);
                    aggrColumns.put(sumColName, MergeCol.MERGE_SUM);//转换sum函数
                    
                    
                    SQLSelectItem count =new SQLSelectItem();
                    String countColName = colName + "COUNT";
                    count.setAlias(countColName);
                    SQLAggregateExpr countExp = new SQLAggregateExpr("COUNT");
                    ObjectUtil.copyProperties(expr,countExp);
                    countExp.getArguments().addAll(expr.getArguments());
                    countExp.setMethodName("COUNT");
                    count.setExpr(countExp);
                    selectList.add(count);
                    aggrColumns.put(countColName, MergeCol.MERGE_COUNT);//转换count函数
                    
         
                    aggrColumns.put(colName, mergeType);
                    ctx.setHasAggrColumn(true);
                } else if (MergeCol.MERGE_UNSUPPORT != mergeType){
					if (item.getAlias() != null && item.getAlias().length() > 0){
						aggrColumns.put(item.getAlias(), mergeType);
					} else{   //如果不加，jdbc方式时取不到正确结果   ;修改添加别名
							item.setAlias(method + i);
							aggrColumns.put(method + i, mergeType);
							
					}
					ctx.setHasAggrColumn(true);
				}
			} else{
				if (!(item.getExpr() instanceof SQLAllColumnExpr)){
					String alia = item.getAlias();
					String field = getFieldName(item);
					if (alia == null){
						alia = field;
					}
					aliaColumns.put(field, alia);
				}
			}

		}
		if(aggrColumns.size() > 0) {
			ctx.setMergeCols(aggrColumns);
		}
        //通过优化转换成group by来实现
        if(isDistinct){
            mysqlSelectQuery.setDistionOption(0);
            SQLSelectGroupByClause   groupBy=new SQLSelectGroupByClause();
            for (String fieldName : aliaColumns.keySet())
            {
                groupBy.addItem(new SQLIdentifierExpr(fieldName));
            }
            mysqlSelectQuery.setGroupBy(groupBy);
        }
        //setGroupByCols
		if(mysqlSelectQuery.getGroupBy() != null) {
			List<SQLExpr> groupByItems = mysqlSelectQuery.getGroupBy().getItems();
			String[] groupByCols = buildGroupByCols(groupByItems,aliaColumns);
			ctx.setGroupByCols(groupByCols);
			ctx.setHavings(buildGroupByHaving(mysqlSelectQuery.getGroupBy().getHaving()));
			ctx.setHasAggrColumn(true);
		}
		return aliaColumns;
	}
	
	
	private HavingCols buildGroupByHaving(SQLExpr having) {
		if (having == null) {
			return null;
		}
		SQLBinaryOpExpr expr  = ((SQLBinaryOpExpr) having);
		SQLExpr left = expr.getLeft();
		SQLBinaryOperator operator = expr.getOperator();
		SQLExpr right = expr.getRight();
		String leftValue = null;
		if (left instanceof SQLAggregateExpr) {
			leftValue = ((SQLAggregateExpr) left).getMethodName() + "("
					+ ((SQLAggregateExpr) left).getArguments().get(0) + ")";
			
		} else if (left instanceof SQLIdentifierExpr) {
			leftValue = ((SQLIdentifierExpr) left).getName();
		}

		String rightValue = null;
		if (right instanceof  SQLNumericLiteralExpr) {
			rightValue = right.toString();
		}else if(right instanceof SQLTextLiteralExpr){
			rightValue = StringUtil.removeBackquote(right.toString());
		}
		return new HavingCols(leftValue,rightValue,operator.getName());
	}


	private String getFieldName(SQLSelectItem item){
		if ((item.getExpr() instanceof SQLPropertyExpr)||(item.getExpr() instanceof SQLMethodInvokeExpr)
				|| (item.getExpr() instanceof SQLIdentifierExpr) || item.getExpr() instanceof SQLBinaryOpExpr) {			
			return item.getExpr().toString();//字段别名
		}else{
		  return item.toString();
		}
	}
	
	private String[] buildGroupByCols(List<SQLExpr> groupByItems,Map<String, String> aliaColumns) {
		String[] groupByCols = new String[groupByItems.size()]; 
		for(int i= 0; i < groupByItems.size(); i++) {
            SQLExpr sqlExpr = groupByItems.get(i);
            String column;
            if(sqlExpr instanceof SQLIdentifierExpr )
            {
                column=((SQLIdentifierExpr) sqlExpr).getName();
            } else
            {
                SQLExpr expr = ((MySqlSelectGroupByExpr) sqlExpr).getExpr();

                if (expr instanceof SQLName)
                {
                    column = StringUtil.removeBackquote(((SQLName) expr).getSimpleName());//不要转大写 2015-2-10 sohudo StringUtil.removeBackquote(expr.getSimpleName().toUpperCase());
                } else
                {
                    column = StringUtil.removeBackquote(expr.toString());
                }
            }
			int dotIndex=column.indexOf(".") ;
			if(dotIndex!=-1)
			{
				//此步骤得到的column必须是不带.的，有别名的用别名，无别名的用字段名
				column=column.substring(dotIndex+1) ;
			}
			groupByCols[i] = getAliaColumn(aliaColumns,column);//column;
		}
		return groupByCols;
	}
	
	
	private String getAliaColumn(Map<String, String> aliaColumns,String column ){
		String alia=aliaColumns.get(column);
		if (alia==null){
			if(column.indexOf(".") < 0) {
				String col = "." + column;
				String col2 = ".`" + column+"`";
				//展开aliaColumns，将<c.name,cname>之类的键值对展开成<c.name,cname>和<name,cname>
				for(Map.Entry<String, String> entry : aliaColumns.entrySet()) {
					if(entry.getKey().endsWith(col)||entry.getKey().endsWith(col2)) {
						if(entry.getValue() != null && entry.getValue().indexOf(".") > 0) {
							return column;
						}
						return entry.getValue();
					}
				}
			}
			
			return column;
		}
		else {
			return alia;
		}
	}
	
	
}
