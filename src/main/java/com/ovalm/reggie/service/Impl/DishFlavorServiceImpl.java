package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.entity.DishFlavor;
import com.ovalm.reggie.mapper.DishFlavorBaseMapper;
import com.ovalm.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorBaseMapper, DishFlavor> implements DishFlavorService {
}
