package com.midea.trade.sharding.client;
import java.io.File;
import java.io.FileNotFoundException;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.ResourceUtils;
import com.midea.trade.sharding.service.TOrderServiceImpl;

public class MybatisTest {
	
	
	static {
		File clsFile;
		try {
			clsFile = ResourceUtils.getFile("classpath:configuration.xml");
			ShardingClient.init(clsFile.getAbsolutePath());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	
	}
	
	@Test
	public void queryTest() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("/testContext.xml");
		TOrderServiceImpl orderService=context.getBean("orderService",TOrderServiceImpl.class);
		orderService.selectQuery();
	}
	
	
	@Test
	public void insertTest() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("/testContext.xml");
		TOrderServiceImpl orderService=context.getBean("orderService",TOrderServiceImpl.class);
		orderService.insert();
	}
	
	
	
	@Test
	public void deleteTest() throws Exception{
		ApplicationContext context = new ClassPathXmlApplicationContext("/testContext.xml");
		TOrderServiceImpl orderService=context.getBean("orderService",TOrderServiceImpl.class);
		orderService.delete();
	}
}
