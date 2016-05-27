package com.midea.trade.sharding.jdbc.parse;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.foundationdb.sql.StandardException;
import com.foundationdb.sql.parser.AndNode;
import com.foundationdb.sql.parser.BinaryOperatorNode;
import com.foundationdb.sql.parser.BinaryRelationalOperatorNode;
import com.foundationdb.sql.parser.ColumnReference;
import com.foundationdb.sql.parser.ConstantNode;
import com.foundationdb.sql.parser.DeleteNode;
import com.foundationdb.sql.parser.InListOperatorNode;
import com.foundationdb.sql.parser.IsNode;
import com.foundationdb.sql.parser.IsNullNode;
import com.foundationdb.sql.parser.JoinNode;
import com.foundationdb.sql.parser.OrNode;
import com.foundationdb.sql.parser.ParameterNode;
import com.foundationdb.sql.parser.SQLParser;
import com.foundationdb.sql.parser.SelectNode;
import com.foundationdb.sql.parser.ValueNode;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.jdbc.nodes.NodeHelper;

/**
 * ValueNodeParser
 * 
 */
public class ValueNodeParser {
	
	public static List<TableColumn> parse(ValueNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		if (valueNode instanceof AndNode) {
			AndNode node = (AndNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		

		if (valueNode instanceof OrNode) {
			OrNode node = (OrNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof BinaryOperatorNode) {
			BinaryOperatorNode node = (BinaryOperatorNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		

		if (valueNode instanceof IsNullNode) {
			IsNullNode node = (IsNullNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof IsNode) {
			IsNode node = (IsNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof BinaryRelationalOperatorNode) {
			BinaryRelationalOperatorNode node = (BinaryRelationalOperatorNode) valueNode;
			results.addAll(parse(node));
			return results;
		}
		if (valueNode instanceof InListOperatorNode) {
			InListOperatorNode node = (InListOperatorNode) valueNode;
			results.addAll(parse(node));
			return results;
		}

		return results;

	}
	
	public static List<TableColumn> parse(JoinNode joinNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		ValueNode joinClause=joinNode.getJoinClause();
		results.addAll(parse(joinClause));
		if(joinNode.getLeftResultSet() instanceof JoinNode){
			results.addAll(parse((JoinNode)joinNode.getLeftResultSet()));
		}
		return results;

	}
	public static List<TableColumn> parse(AndNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		results.addAll(parse(valueNode.getLeftOperand()));
		results.addAll(parse(valueNode.getRightOperand()));
		return results;

	}
	public static List<TableColumn> parse(BinaryOperatorNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		results.addAll(parse(valueNode.getLeftOperand()));
		results.addAll(parse(valueNode.getRightOperand()));
		return results;
	}
	
	public static List<TableColumn> parse(OrNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		results.addAll(parse(valueNode.getLeftOperand()));
		results.addAll(parse(valueNode.getRightOperand()));
		return results;
	}

	public static List<TableColumn> parse(BinaryRelationalOperatorNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();

		ValueNode leftNode = valueNode.getLeftOperand();
		ValueNode rightNode = valueNode.getRightOperand();
		if (leftNode instanceof ConstantNode
				&& rightNode instanceof ConstantNode) {
			return results;
		}
		TableColumn column = new TableColumn();
		if(leftNode instanceof ColumnReference){
			if(rightNode instanceof ConstantNode || rightNode instanceof ParameterNode){
				initColumnValue(column, leftNode);
				column.setValue(NodeHelper.getValue(rightNode));
				results.add(column);
				if(rightNode instanceof ParameterNode){
					ParameterNode parNode=(ParameterNode)rightNode;
					column.setPreparedIndex(parNode.getParameterNumber());
				}
				return results;
			}else{
				results.addAll(parse(rightNode));
			}
		}else if(rightNode instanceof ColumnReference){
			if(leftNode instanceof ConstantNode || leftNode instanceof ParameterNode){
				initColumnValue(column, rightNode);
				column.setValue(NodeHelper.getValue(leftNode));
				results.add(column);
				if(leftNode instanceof ParameterNode){
					ParameterNode parNode=(ParameterNode)leftNode;
					column.setPreparedIndex(parNode.getParameterNumber());
				}
				return results;
			}else{
				results.addAll(parse(leftNode));
			}
		} else {
			results.addAll(parse(leftNode));
			results.addAll(parse(rightNode));
		}
		return results;

	}

	public static List<TableColumn> parse(InListOperatorNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		
		Iterator<ValueNode> valuesIterator=valueNode.getRightOperandList().getNodeList().iterator();
		while(valuesIterator.hasNext()){
			ValueNode item=valuesIterator.next();
			TableColumn column=new TableColumn();
			results.add(column);
			initColumnValue(column, valueNode.getLeftOperand().getNodeList().get(0));
			column.setValue(NodeHelper.getValue(item));
			if(item instanceof ParameterNode){
				ParameterNode parNode=(ParameterNode)item;
				column.setPreparedIndex(parNode.getParameterNumber());
			}
		}
		return results;

	}

	public static List<TableColumn> parse(IsNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		TableColumn column = new TableColumn();
		initColumnValue(column, valueNode.getLeftOperand());
		results.add(column);
		column.setValue(NodeHelper.getValue((ConstantNode) valueNode
				.getRightOperand()));
		return results;
	}

	public static List<TableColumn> parse(IsNullNode valueNode) {
		List<TableColumn> results = new LinkedList<TableColumn>();
		TableColumn column = new TableColumn();
		initColumnValue(column, valueNode.getOperand());
		results.add(column);
		return results;
	}

	public static void initColumnValue(TableColumn column, ValueNode valueNode) {
		column.setName(valueNode.getColumnName());
		column.setTable(valueNode.getTableName());
		try {
			column.setSchema(valueNode.getSchemaName());
		} catch (StandardException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception {
		SQLParser parser = new SQLParser();
		DeleteNode node = (DeleteNode) parser
				.parseStatement("delete from orders t where t.id in(1,2)");
		
		List<TableColumn> results = parse(((SelectNode) node.getResultSetNode())
				.getWhereClause());
		System.out.println(results);
		
		node = (DeleteNode) parser
				.parseStatement("delete from orders where id is true");
		results = parse(((SelectNode) node.getResultSetNode()).getWhereClause());
		System.out.println(results);
		
		node = (DeleteNode) parser
				.parseStatement("delete from orders where id = 1 and 1=1 and 2=name");
		results = parse(((SelectNode) node.getResultSetNode()).getWhereClause());
		System.out.println(results);
		
	}

}
