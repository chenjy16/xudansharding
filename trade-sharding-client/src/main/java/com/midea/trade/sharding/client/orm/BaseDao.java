package com.midea.trade.sharding.client.orm;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import com.midea.trade.sharding.client.ShardingClient;


/**
 * 使用ORM功能的DAO基类
 * <p>
 * 提供标准的excute方法，由子类继承后，进行业务封装扩展
 * </p>
 */
public class BaseDao {
	
	public boolean excute(String sql, Object... objects) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = ShardingClient.getConnection();
			ps = connection.prepareStatement(sql);
			if(objects!=null && objects.length>0){
				for(int i=0; i<objects.length; i++){
					ps.setObject(i+1, objects[i]);
				}
			}
			return ps.execute();
		} finally {
			ShardingClient.closeStatement(ps);
			ShardingClient.closeConnection(connection);
		}
	}
	
	public int excuteUpdate(String sql, Object... objects) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		try {
			connection = ShardingClient.getConnection();
			ps = connection.prepareStatement(sql);
			if(objects!=null && objects.length>0){
				for(int i=0; i<objects.length; i++){
					ps.setObject(i+1, objects[i]);
				}
			}
			return ps.executeUpdate();
		} finally {
			ShardingClient.closeStatement(ps);
			ShardingClient.closeConnection(connection);
		}
	}
	
	public <T> List<T> excuteQuery(Class<T> clazz, String sql, Object... objects) throws Exception {
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = ShardingClient.getConnection();
			ps = connection.prepareStatement(sql);
			if(objects!=null && objects.length>0){
				for(int i=0; i<objects.length; i++){
					ps.setObject(i+1, objects[i]);
				}
			}
			rs = ps.executeQuery();
			return populateData(rs, clazz);
		} finally {
			ShardingClient.close(rs, ps, connection);
		}
	}
	
	private <T> List<T> populateData(ResultSet resultSet, Class<T> clazz) throws Exception {
		List<T> dataList = new ArrayList<T>();
		List<Field> fieldList = MappingAnnotationUtil.getAllFields(clazz);

		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnsCount = rsmd.getColumnCount();
		List<String> columnNameList = new ArrayList<String>();
		for(int i = 0; i < columnsCount; i++){
			columnNameList.add(rsmd.getColumnLabel(i+1).toLowerCase());
		}

		while (resultSet.next()) {
			T bean = clazz.newInstance();
			for(Field f : fieldList) {
				String columnName = MappingAnnotationUtil.getDBCloumnName(clazz, f).toLowerCase();
				if(columnNameList.contains(columnName)) {
					Object columnValueObj = null;
					Class<?> filedCls = f.getType();
					
					if(filedCls == int.class || filedCls == Integer.class) {
						columnValueObj = resultSet.getInt(columnName);
					} else if(filedCls == String.class) {
						columnValueObj = resultSet.getString(columnName);
					} else if(filedCls == boolean.class || filedCls == Boolean.class) {
						columnValueObj = resultSet.getBoolean(columnName);
					} else if(filedCls == byte.class || filedCls == Byte.class) {
						columnValueObj = resultSet.getByte(columnName);
					} else if(filedCls == short.class || filedCls == Short.class) {
						columnValueObj = resultSet.getShort(columnName);
					} else if(filedCls == long.class || filedCls == Long.class) {
						columnValueObj = resultSet.getLong(columnName);
					} else if(filedCls == float.class || filedCls == Float.class) {
						columnValueObj = resultSet.getFloat(columnName);
					} else if(filedCls == double.class || filedCls == Double.class) {
						columnValueObj = resultSet.getDouble(columnName);
					} else if(filedCls == BigDecimal.class) {
						columnValueObj = resultSet.getBigDecimal(columnName);
					} 
					
					else {
						columnValueObj = resultSet.getObject(columnName);
					}
					
					if (columnValueObj != null) {
						Method setterMethod = MappingAnnotationUtil.getSetterMethod(clazz, f);
						setterMethod.invoke(bean, new Object[] { columnValueObj });
					}
				}
			}
			dataList.add(bean);
		}
		return dataList;
	}

}