package com.midea.trade.sharding.client;
import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;


import java.sql.SQLException;
import java.sql.Statement;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.midea.trade.sharding.entity.TOrderMain;
import com.midea.trade.sharding.jdbc.ConnectionWrapper;
import com.midea.trade.sharding.mapper.TOrderMainMapper;



public class JdbcTest {
	
	

	
	static {
		// 进程启动时要对进行初始化(主要是配置初始化)
		ShardingClient.init("d:/configuration.xml");
	}
	
	
	
	@Test
	public void queryTest() throws Exception{
		//System.out.println(Thread.currentThread().getContextClassLoader().getResource(""));
		
		// Resource res2 = new ClassPathResource("conf/file1.txt");   
		//File clsFile = ResourceUtils.getFile("classpath:conf/file1.txt"); 
		String sql = "select * from  m_shop_test_rw where shop_id in(54251,54252,54253) ";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = ShardingClient.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getInt(1)+"--"+rs.getString(2)+"--"+rs.getString(3));
			}
		} finally {
			ShardingClient.close(rs, ps, connection);
		}
	}
	
	
	@Test
	public void testSelectDistinct() throws SQLException {
		
		String sql = "select distinct (shop_name) from m_shop_test_rw";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = ShardingClient.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getInt(1));
			}
		} finally {
			ShardingClient.close(rs, ps, connection);
		}
	}
	
	
	
	@Test
	public void testSelectVisual() throws SQLException {
		String sql = "select count(*) from m_shop_test_rw  where id>1 ";
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			connection = ShardingClient.getConnection();
			ps = connection.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				System.out.println(rs.getInt(1));
			}
		} finally {
			ShardingClient.close(rs, ps, connection);
		}
	}
	
	
	
	

}
