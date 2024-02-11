package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.entity.SetmealDish;
import com.ovalm.reggie.mapper.SetmealDishBaseMapper;
import com.ovalm.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishBaseMapper, SetmealDish> implements SetmealDishService {
}
