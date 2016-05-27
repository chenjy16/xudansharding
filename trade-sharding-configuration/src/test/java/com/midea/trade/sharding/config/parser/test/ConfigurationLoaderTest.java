package com.midea.trade.sharding.config.parser.test;
import java.io.File;
import java.net.URL;

import org.junit.Test;

import com.midea.trade.sharding.config.parser.ConfigurationLoader;

public class ConfigurationLoaderTest {
	
/*	static {
		try {
			URL url = ConfigurationLoaderTest.class.getClassLoader().getResource("configurations_demo.xml");
			File file = new File(url.toURI());
			Configurations.getInstance().init(file.getAbsolutePath());
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
	}*/
	
	
	@Test
	public void  loadTest()throws Exception{
		ConfigurationLoader loader=new ConfigurationLoader();
		URL url = ConfigurationLoaderTest.class.getClassLoader().getResource("configuration.xml");
		File file = new File(url.toURI());
		loader.load(file.getAbsolutePath());
	}

}
