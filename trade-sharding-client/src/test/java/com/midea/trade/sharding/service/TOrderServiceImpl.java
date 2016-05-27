package com.midea.trade.sharding.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.midea.trade.sharding.entity.TOrderMain;
import com.midea.trade.sharding.entity.TOrderMainExample;
import com.midea.trade.sharding.mapper.TOrderMainMapper;


public class TOrderServiceImpl implements TOrderService{
	
	
	@Autowired
	private  TOrderMainMapper orderMainMapper;
	
	
	public void  selectQuery(){
		TOrderMainExample example=new TOrderMainExample();
		//example.createCriteria().andShopIdEqualTo(115).andPlatformIdEqualTo(Byte.valueOf("1"));
		example.createCriteria().andOrderIdEqualTo(2015033100000003L);
		List<TOrderMain> list=orderMainMapper.selectByExample(example);
		System.out.println(JSON.toJSONString(list));
	}
	
	
	
	
	public void  insert(){
		TOrderMainExample example=new TOrderMainExample();
		//example.createCriteria().andShopIdEqualTo(115).andPlatformIdEqualTo(Byte.valueOf("1"));
		example.createCriteria().andOrderIdEqualTo(2015033100000003L);
		List<TOrderMain> list=orderMainMapper.selectByExample(example);
		System.out.println(JSON.toJSONString(list.get(0)));
		orderMainMapper.insertSelective(list.get(0));
		
	}
	
	
	public void  delete(){
		TOrderMainExample example=new TOrderMainExample();
		//example.createCriteria().andShopIdEqualTo(115).andPlatformIdEqualTo(Byte.valueOf("1"));
		example.createCriteria().andOrderIdEqualTo(2015033100000001L);
		int count=orderMainMapper.deleteByExample(example);
		System.out.println(count);
	}
	

}
