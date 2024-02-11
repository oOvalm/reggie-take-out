package com.ovalm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ovalm.reggie.entity.Orders;

public interface OrdersService extends IService<Orders> {
    void submit(Orders orders);
}
