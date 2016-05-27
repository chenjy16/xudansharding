package com.midea.trade.sharding.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.midea.trade.sharding.client.annotation.IRepository;
import com.midea.trade.sharding.entity.TOrderMain;
import com.midea.trade.sharding.entity.TOrderMainExample;



@IRepository
public interface TOrderMainMapper {
	
	int countByExample(TOrderMainExample example);

	int deleteByExample(TOrderMainExample example);

	int deleteByPrimaryKey(Long orderId);

	int insert(TOrderMain record);

	int insertSelective(TOrderMain record);

	List<TOrderMain> selectByExample(TOrderMainExample example);

	TOrderMain selectByPrimaryKey(Long orderId);

	int updateByExampleSelective(@Param("record") TOrderMain record,
			@Param("example") TOrderMainExample example);

	int updateByExample(@Param("record") TOrderMain record,
			@Param("example") TOrderMainExample example);

	int updateByPrimaryKeySelective(TOrderMain record);

	int updateByPrimaryKey(TOrderMain record);

	
}