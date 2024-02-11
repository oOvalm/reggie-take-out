package com.ovalm.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ovalm.reggie.entity.SetmealDish;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.jmx.export.annotation.ManagedOperationParameter;

@Mapper
public interface SetmealDishBaseMapper extends BaseMapper<SetmealDish> {
}
