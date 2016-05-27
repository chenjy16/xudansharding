package com.midea.trade.sharding.jdbc.conection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Test;
import com.midea.trade.sharding.config.Configurations;
import com.midea.trade.sharding.jdbc.ConnectionWrapper;



public class mergerTest {
	
	
	static {
		Configurations.getInstance().init("d:/configuration.xml");
	}
	public Connection getConnection() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://172.16.14.168:3306/ins_tc_prd", "root", "Midea_2014@TC");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	/**
	* @author chejy 
	* @Description:avg函数聚合
	* @throws SQLException
	* @return void    返回类型
	* @throws
	 */
	@Test
	public void testAvg() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		String sql = "SELECT AVG(STATUS) FROM m_shop_test_rw";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}
	
	
	
	/**
	* @author chejy 
	* @Description: min 聚合
	* @throws SQLException
	* @return void    返回类型
	* @throws
	 */
	@Test
	public void testMin() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		String sql = "SELECT min(STATUS) FROM m_shop_test_rw";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.println("]");
		}
	}
	
	
	
	
	/**
	* @author chejy 
	* @Description: max聚合       如果有group by,select后面也要带上这个列名
	* @throws SQLException
	* @return void    返回类型
	*/
	@Test
	public void testCount() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		String sql = "SELECT shop_name,COUNT(STATUS) FROM m_shop_test_rw  GROUP BY shop_name  ORDER BY  COUNT(STATUS)";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.println("]");
		}
	}
	
	
	
	/**
	* @author chejy 
	* @Description: limit 聚合
	* @throws SQLException
	* @return void    返回类型
	* @throws
	 */
	@Test
	public void testPage() throws SQLException {
		Connection conn = this.getConnection();
		ConnectionWrapper wrapper = new ConnectionWrapper(conn);
		String sql = "SELECT * FROM m_shop_test_rw limit 0,10";
		PreparedStatement statement = wrapper.prepareStatement(sql);
		ResultSet result = statement.executeQuery();
		while (result.next()) {
			System.out.print("result:[");
			System.out.print(result.getString(1) + ",");
			System.out.print(result.getString(2) + ",");
			System.out.print(result.getString(3) + ",");
			System.out.println("]");
		}
	}

}

