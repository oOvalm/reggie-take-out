package com.ovalm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ovalm.reggie.entity.ShoppingCart;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ShoppingCartBaseMapper extends BaseMapper<ShoppingCart> {
}
