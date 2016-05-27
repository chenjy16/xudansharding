package com.midea.trade.sharding.core.util;

import java.sql.Connection;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.midea.trade.sharding.core.context.ConnectionContext;
import com.midea.trade.sharding.core.resources.DataNode;
import com.midea.trade.sharding.core.shard.TableColumn;
import com.midea.trade.sharding.core.tx.Transaction;

/**
 * ConnectionUtils
 */
public class ConnectionUtils {
	static final Map<String, TableColumn[]> tableInfos = new HashMap<String, TableColumn[]>();

	public static TableColumn[] getTableConlumns(Connection connection,
			String schema, String tableName) throws SQLException {
		try {
			TableColumn[] results = tableInfos.get(tableName.toUpperCase());
			if (results != null) {
				return results;
			}
			DatabaseMetaData dbMeata = connection.getMetaData();

			if (schema == null) {
				schema = "%";
			}
			ResultSet resultSet = dbMeata.getColumns(null, schema, tableName,
					"%");
			List<TableColumn> columns = new LinkedList<TableColumn>();
			int i = 0;
			while (resultSet.next()) {
				TableColumn column = new TableColumn();
				column.setName(resultSet.getString("COLUMN_NAME"));
				column.setResultIndex(i++);
				columns.add(column);
			}

			results = columns.toArray(new TableColumn[] {});
			tableInfos.put(tableName.toUpperCase(), results);
			resultSet.close();
			return results;
		} finally {
			connection.close();
		}
	}

	public static TableColumn[] getTableConlumns(Connection connection,
			String tableName) throws SQLException {
		return getTableConlumns(connection, "%", tableName);
	}

	public static TableColumn[] getTableConlumns(DataNode node, String tableName)
			throws SQLException {
		TableColumn[] results = tableInfos.get(tableName.toUpperCase());
		if (results != null) {
			return results;
		}
		return getTableConlumns(node.getConnection(), "%", tableName);
	}

	public static Transaction getTransaction() {
		return ConnectionContext.getContext().getTransaction();
	}
}
