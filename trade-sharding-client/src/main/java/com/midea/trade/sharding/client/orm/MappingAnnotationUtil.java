package com.midea.trade.sharding.client.orm;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.midea.trade.sharding.client.entity.ClassInfo;

final class MappingAnnotationUtil {

	private static Map<Class<?>, ClassInfo> classInfoCache = Maps.newHashMap();
	
	public static Map<Class<?>, ClassInfo> getAllClassInfo() {
		return classInfoCache;
	}
	
	private static ClassInfo getClassInfo(Class<?> clazz){
		ClassInfo ci = classInfoCache.get(clazz);
		if(ci == null) {
			try {
				ci = new ClassInfo(clazz);
				classInfoCache.put(clazz, ci);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
		}
		return ci;
	}

	/**
	 * 获得字段对应的set方法
	 */
	public static Method getSetterMethod(Class<?> clazz, Field field) throws Exception {
		ClassInfo ci = getClassInfo(clazz);
		Map<String, Method> mapSetterMethod = ci.getMapSetMethod();
		return mapSetterMethod.get(field.getName());
	}
	
	/**
	 * 获得字段对应的get方法
	 */
	public static Method getGetterMethod(Class<?> clazz, Field field) {
		ClassInfo ci = getClassInfo(clazz);
		Map<String, Method> mapGetterMethod = ci.getMapGetMethod();
		return mapGetterMethod.get(field.getName());
	}
	
	/**
	 * 获得所有字段
	 */
	public static List<Field> getAllFields(Class<?> clazz) {
		ClassInfo ci = getClassInfo(clazz);
		Collection<Field> coll = ci.getMapAllDBField().values();
		List<Field> fields = Lists.newArrayList();
		for(Field f : coll) {
			fields.add(f);
		}
		return fields;
	}
	
	/**
	 * 获得字段名
	 */
	public static String getDBCloumnName(Class<?> clazz, Field f){
		ClassInfo ci = getClassInfo(clazz);
		return ci.getMapDBColumnName().get(f.getName());
	}
}

