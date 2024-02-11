package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.entity.ShoppingCart;
import com.ovalm.reggie.mapper.ShoppingCartBaseMapper;
import com.ovalm.reggie.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartBaseMapper, ShoppingCart> implements ShoppingCartService {

}
