package com.midea.trade.sharding.core.util;

import java.beans.IntrospectionException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.ConvertUtils;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * 通过反射对规范的JavaBean做set、get方法调用
 */
public class BeanUtils {
	
	private static final Map<Class<?>, BeanInfo> BEAN_CACHE = Maps.newConcurrentMap();
	
	public static void setProperty(Object object, String name, Object value) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		BeanInfo beanInfo = getBeanInfo(object);
		
		Method setter = beanInfo.getSetterMapping().get(name);
		if(setter == null)
			return;
		
		setter.invoke(object, ConvertUtils.convert(value, setter.getParameterTypes()[0]));
	}
	
	public static Object getProperty(Object object, String name) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		BeanInfo beanInfo = getBeanInfo(object);
		
		Method getter = beanInfo.getGetterMapping().get(name);
		
		if(getter == null)
			return null;
		
		return getter.invoke(object);
	}
	
	private static BeanInfo getBeanInfo(Object object) {
		BeanInfo beanInfo = BEAN_CACHE.get(object.getClass());
		if(beanInfo == null){
			try {
				beanInfo = new BeanInfo(object.getClass());
				BEAN_CACHE.put(object.getClass(), beanInfo);
			} catch (IntrospectionException e) {
				e.printStackTrace();
			}
		}
		return beanInfo;
	}
	
	private static class BeanInfo {
		private Class<?> bean;
		private Set<Field> fieldSet = Sets.newHashSet();
		private Map<String, Field> fieldMapping = Maps.newHashMap();
		private Map<String, Method> setterMapping = Maps.newHashMap();
		private Map<String, Method> getterMapping = Maps.newHashMap();
		
		BeanInfo(Class<?> bean) throws IntrospectionException {
			this.bean = bean;
			parseFieldMapping();
			parseSetterMapping();
			parseGetterMapping();
		}

		private void parseFieldMapping() {
			Field[] fields = bean.getDeclaredFields();
			for (Field field : fields) {
				if("serialVersionUID".equalsIgnoreCase(field.getName())){
					continue;
				}
				fieldMapping.put(field.getName(), field);
				fieldSet.add(field);
			}
		}
		
		private void parseSetterMapping() throws IntrospectionException {
			Map<String, Method> mapMethod = new HashMap<String, Method>();
			
			for(Field f : fieldSet) {
				Method setterMethod = mapMethod.get(f.getName());
				if(setterMethod == null) {
					String setFunName = "set" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
			
					for(Method m : bean.getMethods()){
						if(m.getName().equals(setFunName)){
							setterMethod = m;
							break;
						}
					}
					
					mapMethod.put(f.getName(), setterMethod);
				}
			}
			
			setterMapping.putAll(mapMethod);
		}
		
		private void parseGetterMapping() throws IntrospectionException {
			Map<String, Method> mapMethod = new HashMap<String, Method>();
			
			for(Field f : fieldSet) {
				Method getterMethod = mapMethod.get(f.getName());
				if(getterMethod == null) {
					String getFunName = "get" + f.getName().substring(0,1).toUpperCase()+ f.getName().substring(1);
			
					for(Method m : bean.getMethods()){
						if(m.getName().equals(getFunName)){
							getterMethod = m;
							break;
						}
					}
					
					mapMethod.put(f.getName(), getterMethod);
				}
			}
			
			getterMapping.putAll(mapMethod);
		}

		Map<String, Method> getSetterMapping() {
			return setterMapping;
		}

		Map<String, Method> getGetterMapping() {
			return getterMapping;
		}
		
	}

}
