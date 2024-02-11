package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.entity.OrderDetail;
import com.ovalm.reggie.mapper.OrderDetailBaseMapper;
import com.ovalm.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;

@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailBaseMapper, OrderDetail> implements OrderDetailService {
}
