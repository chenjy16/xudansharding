package com.midea.trade.sharding.druid.expr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.midea.trade.sharding.core.shard.AnalyzeResult;

public class SQLBinaryOpExprAnalyzer {
	
	static Logger logger = LoggerFactory.getLogger(SQLBinaryOpExprAnalyzer.class);
	
	int[] nodeTypes = {SQLBinaryOperator.Equality.getPriority(),
			SQLBinaryOperator.NotEqual.getPriority(),
			SQLBinaryOperator.GreaterThan.getPriority(),
			SQLBinaryOperator.GreaterThanOrEqual.getPriority(),
			SQLBinaryOperator.LessThan.getPriority(),
			SQLBinaryOperator.LessThanOrEqual.getPriority(),
			SQLBinaryOperator.Add.getPriority(),
			SQLBinaryOperator.Multiply.getPriority(),
			SQLBinaryOperator.Divide.getPriority(),
			SQLBinaryOperator.Subtract.getPriority(), 
			SQLBinaryOperator.Modulus.getPriority()};
	
	
	
	public AnalyzeResult doAnalyze(SQLBinaryOpExpr node){
		SQLExpr left=node.getLeft();
		SQLExpr right=node.getRight();
		
		//SQLLiteralExpr,SQLValuableExpr
		if(left instanceof  SQLValuableExpr && right instanceof SQLValuableExpr){
			
		}
		if(left instanceof  SQLValuableExpr){
			
		}else if(right instanceof SQLValuableExpr){
			
		}else{
			
		}
		return null;
	}

}
