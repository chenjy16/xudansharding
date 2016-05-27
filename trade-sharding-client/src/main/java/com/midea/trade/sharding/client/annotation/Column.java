package com.midea.trade.sharding.client.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 列注解
 * <p>
 * 标注当前成员变量对应的列名，以及对应的set、get方法
 * </p>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Column {
	
	/**
	 * DB table 列名
	 * @return
	 */
	public String name() default "fieldName";
	
	/**
	 * 定义特殊set方法名
	 * @return
	 */
	public String setFuncName() default "setField";
	
	/**
	 * 定义特殊get方法名
	 * @return
	 */
	public String getFuncName() default "getField";
	
}