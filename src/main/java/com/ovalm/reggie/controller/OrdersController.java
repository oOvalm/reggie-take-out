package com.ovalm.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ovalm.reggie.common.BaseContext;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.entity.Orders;
import com.ovalm.reggie.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrdersController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     * @return
     */
    @RequestMapping("/submit")
    public R submit(@RequestBody Orders orders) {
        log.info("下单: {}", orders);
        ordersService.submit(orders);
        return R.ok().put("data", "下单成功");

    }

    @GetMapping("/userPage")
    public R queryPage(int page, int pageSize) {
        Long userId = BaseContext.getCurrentId();
        Page<Orders> pageInfo = new Page<>(page, pageSize);

        QueryWrapper<Orders> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByDesc("order_time");

        ordersService.page(pageInfo, wrapper);
        return R.ok().put("data", pageInfo);
    }
}
