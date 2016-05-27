package com.midea.trade.sharding.jdbc.results.merger;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.script.ScriptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.midea.trade.sharding.core.context.StatementContext;
import com.midea.trade.sharding.core.exception.ShardException;
import com.midea.trade.sharding.core.jdbc.aggregate.Aggregate;
import com.midea.trade.sharding.core.jdbc.aggregate.Avg;
import com.midea.trade.sharding.core.jdbc.aggregate.Count;
import com.midea.trade.sharding.core.jdbc.aggregate.Max;
import com.midea.trade.sharding.core.jdbc.aggregate.Min;
import com.midea.trade.sharding.core.jdbc.aggregate.Sum;
import com.midea.trade.sharding.core.jdbc.result.ColumnFinder;
import com.midea.trade.sharding.core.jdbc.result.GroupByKeyGenerator;
import com.midea.trade.sharding.core.jdbc.result.RowSet;
import com.midea.trade.sharding.core.jdbc.result.RowSetComparator;
import com.midea.trade.sharding.core.jdbc.result.RowSetCreator;
import com.midea.trade.sharding.core.jdbc.result.RowSetsResultSet;
import com.midea.trade.sharding.core.jdbc.result.SimpleMergedResultSet;
import com.midea.trade.sharding.core.script.InterpretedScriptExecutor;
import com.midea.trade.sharding.core.script.ScriptExecutor;
import com.midea.trade.sharding.core.shard.AnalyzeResult;
import com.midea.trade.sharding.core.shard.HavingInfo;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.util.StringUtils;



/**
 * max(name),min(name),avg(name),count(name),sum(name),count(*),name
 * from table group by name 对聚合函数来说按照以下的逻辑：
 * 1.group by，当前结果
 */
@SuppressWarnings("rawtypes")
public class DefaultResultSetMerger implements ResultSetMerger {
	
	static Logger logger = LoggerFactory
			.getLogger(DefaultResultSetMerger.class);
	
	RowSetCreator rowSetCreator = new DefaultRowSetCreator();
	
	ScriptExecutor<Boolean> scriptExecutor = new InterpretedScriptExecutor<Boolean>();

	@Override
	public ResultSet merge(final ResultSet[] resultSets,
			StatementContext context) throws SQLException {
		final AnalyzeResult analyzeResult = context.getCurrentBatch()
				.getAnalyzeResult();
		final Collection<TableColumn> groupByColumns = analyzeResult
				.getGroupByColumns();
		final Collection<TableColumn> resultColumns = analyzeResult
				.getResultColumns();
		final Collection<TableColumn> orderByColumns = analyzeResult
				.getOrderByColumns();
		final ResultSetMetaData metaData = resultSets[0].getMetaData();
		if (groupByColumns.isEmpty() && orderByColumns.isEmpty()
				&& analyzeResult.getLimit() == null
				&& analyzeResult.getAggregateColumns().isEmpty()
				&& !analyzeResult.isDistinct()) {
			return new SimpleMergedResultSet(Arrays.asList(resultSets));
		}
		//分组的列
		GroupByKeyGenerator groupByKeyGenerator = null;
		RowSetComparator comparator = null;
		if (!groupByColumns.isEmpty() || analyzeResult.isDistinct()) {
			if (analyzeResult.isDistinct()) {
				groupByKeyGenerator = new GroupByKeyGenerator(resultColumns.toArray(new TableColumn[] {}));
			} else {
				groupByKeyGenerator = new GroupByKeyGenerator(groupByColumns.toArray(new TableColumn[] {}));
			}
		}
		ResultSetColumnFinder columnFinder = new ResultSetColumnFinder(resultSets[0]);
		
		for (TableColumn column : groupByColumns) {
			if (column.getAliasName() != null&& column.getAliasName().trim().length() > 0) {
				column.setResultIndex(columnFinder.findIndex(column.getAliasName()));
			} else {
				column.setResultIndex(columnFinder.findIndex(column.getName()));
			}
		}
		for (TableColumn column : orderByColumns) {
			if (column.getAliasName() != null&& column.getAliasName().trim().length() > 0) {
				column.setResultIndex(columnFinder.findIndex(column.getAliasName()));
			} else {
				column.setResultIndex(columnFinder.findIndex(column.getName()));
			}
		}
		if (!orderByColumns.isEmpty()) {
			comparator = new RowSetComparator(orderByColumns.toArray(new TableColumn[] {}));
		}
		Map<Object, RowSet> groupByMap = new LinkedHashMap<Object, RowSet>();
		Map<String, Aggregate> aggregateMap = new HashMap<String, Aggregate>();
		List<RowSet> results = new LinkedList<RowSet>();
		int rowCounts[] = new int[resultSets.length];// 记录每个结果集的行数，通过行数判断，假如每个结果集的行数是1，并且包含了聚集函数，没有group
														// by情况下，则合并成一行
		for (int i = 0; i < resultSets.length; i++) {// 不同的resultset进行处理，每个resultset的结果需要合并一下
			final ResultSet resultSet = resultSets[i];
			while (resultSet.next()) {
				rowCounts[i]++;
				RowSet rowSet = rowSetCreator.create(resultSet, columnFinder);
				String key = "";
				if (groupByKeyGenerator != null) {//存在groupby关键字
					//分组列值+聚合函数列名+聚合函数列名
					Object groupKey = groupByKeyGenerator.getKey(rowSet);//group by  列的值
					RowSet groupedRowset = groupByMap.get(groupKey);
					if (groupedRowset != null) {
						if (analyzeResult.isDistinct()) {
							continue;
						}
						processAggregate(rowSet, resultSet, analyzeResult,groupKey, aggregateMap);
						continue;
					}
					//group by列的值---->行对象  ，此结构用于去重
					groupByMap.put(groupKey, rowSet);
					if (groupKey != null) {
						key = groupKey.toString();
					}
				}
				processAggregate(rowSet, resultSet, analyzeResult, key,aggregateMap);
				results.add(rowSet);
			}
			resultSet.close();
		}
		if (comparator != null) {
			Collections.sort(results, comparator);
		}
		results = processLimit(results, analyzeResult);
		if (analyzeResult.getGroupByColumns().isEmpty()
				&& analyzeResult.getAggregateColumns().size() > 0) {// 在查询结果中有聚集函数，而且每个结果集返回<=1
			boolean mergeToOne = true;
			for (int i = 0; i < rowCounts.length; i++) {
				if (rowCounts[i] > 1) {
					mergeToOne = false;
					break;
				}
			}
			if (mergeToOne) {
				results = results.subList(0, 1);
			}
		}
		this.processHaving(results, analyzeResult);
		return new RowSetsResultSet(results, metaData);
	}

	
	
	/**
	* @author chejy 
	* @Description:处理limit聚合
	* @param results
	* @param analyzeResult
	* @return List<RowSet>    返回类型
	* @throws
	 */
	private List<RowSet> processLimit(List<RowSet> results,AnalyzeResult analyzeResult) {
		if (analyzeResult.getLimit() == null) {
			return results;
		}
		int limit = analyzeResult.getLimit().getValue();
		int offset = 0;
		if (analyzeResult.getOffset() != null) {
			offset = analyzeResult.getOffset().getValue();
		}
		if(offset>=results.size()){
			return Collections.EMPTY_LIST;
		}
		int pos=offset + limit;
		if(pos>results.size()){
			pos=results.size(); 
		}
		return results.subList(offset, pos);

	}

	
	/**
	* @author chejy 
	* @Description: group by后聚合      还要在having过滤
	* @param results
	* @param analyzeResult
	* @return List<RowSet>    返回类型
	* @throws
	 */
	private List<RowSet> processHaving(List<RowSet> results,
			AnalyzeResult analyzeResult) {
		if (analyzeResult.getHavingInfo() == null || results == null|| results.isEmpty()) {
			
			return results;
		}
		Map<String, TableColumn> columnsMap = new HashMap<String, TableColumn>();
		for (TableColumn column : analyzeResult.getResultColumns()) {
			String key = this.getTableColumnKey(column);
			columnsMap.put(column.getName(), column);
			columnsMap.put(key, column);
		}
		HavingInfo havingInfo = analyzeResult.getHavingInfo();
		
		for (TableColumn column : havingInfo.getAggregateColumns()) {
			String key = this.getTableColumnKey(column);
			TableColumn resultColumn = columnsMap.get(key);
			if (resultColumn == null) {
				key = column.getAggregateNodeContent();
				resultColumn = columnsMap.get(key);
			}
			column.setResultIndex(resultColumn.getResultIndex());
			column.setValue(resultColumn.getValue());
		}
		Iterator<RowSet> iterator = results.iterator();
		String scriptExp = getScriptExp(havingInfo);
		while (iterator.hasNext()) {
			RowSet row = iterator.next();
			Map<String, Object> contextMap = this.createScriptContextMap(
					columnsMap, row);
			try {
				if (!scriptExecutor.execute(scriptExp, contextMap)) {
					iterator.remove();
				}
			} catch (ScriptException e) {
				logger.error("script execute error!", e);
				throw new ShardException("having error!havingInfo="
						+ havingInfo, e);
			}
		}
		return results;

	}
	
	private String getTableColumnKey(TableColumn column){
		StringBuilder builder = new StringBuilder(column.getName());
		
		if(column.getTable() != null){
			builder.insert(0, ".");
			builder.insert(0, column.getTable());
		}
		
		if(column.getAggregate() != null){
//			builder.insert(0, column.getAggregate());
//			builder.insert(column.getAggregate().length(), "(");
//			builder.append(")");
			builder.delete(0, builder.length());
			builder.append(column.getAggregateNodeContent().replaceAll("[\\[|\\]]", ""));
		}
		return builder.toString();
	}
	
	
	
	/**
	* @author chejy 
	* @Description: js语句参数封装
	* @param columnsMap
	* @param row
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	private Map<String, Object> createScriptContextMap(
			Map<String, TableColumn> columnsMap, RowSet row) {
		Map<String, Object> context = new HashMap<String, Object>();
		for (Iterator<Entry<String, TableColumn>> it = columnsMap.entrySet().iterator();it.hasNext();) {
			
			Entry<String, TableColumn> entry = it.next();
			Object value;
			try {
				if(row.getObject(entry.getKey()) != null){
					value = row.getObject(entry.getValue().getResultIndex());
					
					context.put(entry.getKey().toUpperCase(), value);
					context.put("$" + entry.getValue().getResultIndex(), value);
				}
			} catch (SQLException e) {
				logger.error("get value error!", e);
				throw new ShardException("having error!column=" + entry.getValue(), e);
			}
		}

		return context;

	}
	
	/**
	* @author chejy 
	* @Description: having子句转换成js语句   
	* @param havingInfo
	* @return
	* @return String    返回类型
	* @throws
	 */
	private String getScriptExp(HavingInfo havingInfo) {
		String exp = havingInfo.getExpression().toUpperCase();
		for (TableColumn column : havingInfo.getAggregateColumns()) {
			//替换后：$1
			exp = exp.replaceAll(column.getAggregateNodeContent(),"\\$" + column.getResultIndex());
			if (StringUtils.isNotBlank(column.getAliasName())) {
				
				exp = exp.replaceAll(column.getAliasName().toUpperCase(), "\\$"+ column.getResultIndex());
			}
		}
		exp = MergeUtils.getScriptExp(exp);
		return exp;

	}

	boolean havingCondition(String exp, Map<String, Object> contextMap) {

		return false;
	}
	
	
	
	/**
	* @author chejy 
	* @Description:
	* 1、创建聚合函数对象  
	* 2、<聚合查询结果集，聚合查询结果值>:因为是聚合函数,所以只保证一个结果集,有一个value
	* 3、将结果集中的行记录对象和聚合函数绑定
	* @param rowSet
	* @param resultSet
	* @param analyzeResult
	* @param groupKey
	* @param aggregateMap
	* @throws SQLException
	* @return void    返回类型
	 */
	private void processAggregate(RowSet rowSet, ResultSet resultSet,
			AnalyzeResult analyzeResult, Object groupKey,
			Map<String, Aggregate> aggregateMap) throws SQLException {
		
		Collection<TableColumn> aggregateColumns = analyzeResult.getAggregateColumns();
		//存储聚合函数平均值列
		List<TableColumn> avgColumns = new ArrayList<TableColumn>();
		Set<Aggregate> aggregates = new HashSet<Aggregate>();
		for (TableColumn column : aggregateColumns) {
			if (column.getAggregate().toUpperCase().startsWith("AVG")|| column.getAggregate().toUpperCase().startsWith("AVERAGE")) {// avg延后初始化
				avgColumns.add(column);
				continue;
			}
			//创建聚合函数对象
			Aggregate function = this.createAggregate(column, groupKey,aggregateMap);
			//存储聚合函数集合
			if (!aggregates.contains(function)) {// 假如sql中存在多个聚集函数，过滤掉，select
				aggregates.add(function);
				function.addRow(resultSet, rowSet);
			}
			rowSet.setAggregate(column.getResultIndex(), function);
		}
		//只处理avg聚合函数
		for (TableColumn column : avgColumns) {
			Aggregate function = this.createAggregate(column, groupKey,aggregateMap);
			function.addRow(resultSet, rowSet);
			rowSet.setAggregate(column.getResultIndex(), function);
		}
	}

	
	
	private String getAggregateKey(TableColumn column, Object groupKey) {
		String key = groupKey + column.getAggregate().toUpperCase()
				+ column.getName();
		return key.toUpperCase();
	}

	private Aggregate createAggregate(TableColumn column, Object groupKey,
			Map<String, Aggregate> aggregateMap) {
		String aggregate = column.getAggregate();
		String key = this.getAggregateKey(column, groupKey);
		Aggregate function = aggregateMap.get(key);
		if (function != null) {
			return function;
		}
		if (aggregate.toUpperCase().startsWith("COUNT")) {
			function = new Count(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("MAX")) {
			function = new Max(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("MIN")) {
			function = new Min(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("SUM")) {
			function = new Sum(column.getName(), key, column.getResultIndex());
		}
		if (aggregate.toUpperCase().startsWith("AVG")
				|| aggregate.toUpperCase().startsWith("AVERAGE")) {
			String sumKey = (groupKey + "SUM" + column.getName()).toUpperCase();
			String countKey = (groupKey + "COUNT" + column.getName()).toUpperCase();
			Sum sum = (Sum) aggregateMap.get(sumKey);
			Count count = (Count) aggregateMap.get(countKey);
			function = new Avg(sum, count, key, column.getResultIndex());
		}
		if (!aggregateMap.containsKey(key)) {
			aggregateMap.put(key, function);
		}
		return function;
	}

	class ResultSetColumnFinder implements ColumnFinder {
		ResultSet resultSet;
		ResultSetMetaData metaData;
		Map<String, Integer> values = new HashMap<String, Integer>();

		public ResultSetColumnFinder(ResultSet rs) throws SQLException {
			this.resultSet = rs;
			this.metaData = rs.getMetaData();
			init();
		}

		private void init() throws SQLException {
			int count = metaData.getColumnCount();
			for (int i = 1; i <= count; i++) {
				String label = metaData.getColumnLabel(i);
				String name = metaData.getColumnName(i);
				String key = label.toUpperCase();
				values.put(key, i);
				key = name.toUpperCase();
				if (!values.containsKey(key)) {
					values.put(key, i);
				}
			}
		}

		@Override
		public int findIndex(String columnName) throws SQLException {
			String key = columnName.toUpperCase();
			if (values.containsKey(key)) {
				return values.get(key);
			}
			Integer value = resultSet.findColumn(columnName);
			values.put(key, value);
			return value;
		}

	};

}
