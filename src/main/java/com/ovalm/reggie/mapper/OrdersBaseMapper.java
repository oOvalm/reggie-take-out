package com.ovalm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ovalm.reggie.entity.Orders;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrdersBaseMapper extends BaseMapper<Orders> {
}
