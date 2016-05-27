package com.midea.trade.sharding.core.util.test;

import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import com.midea.trade.sharding.core.util.BeanUtils;

public class BeanUtilsTest {
	
	@Test
	public void setterTest() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException{
		UserBean bean = new UserBean();
		BeanUtils.setProperty(bean, "uid", 1);
		BeanUtils.setProperty(bean, "name", "张三");
		BeanUtils.setProperty(bean, "salary", 2000.0);
		BeanUtils.setProperty(bean, "addresses", new String[]{"cn","bj"});
		
		System.out.println(BeanUtils.getProperty(bean, "uid"));
		System.out.println(BeanUtils.getProperty(bean, "name"));
		System.out.println(BeanUtils.getProperty(bean, "salary"));
		System.out.println(BeanUtils.getProperty(bean, "addresses"));
		
	}

}
