package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.common.BaseContext;
import com.ovalm.reggie.common.CustomException;
import com.ovalm.reggie.entity.*;
import com.ovalm.reggie.mapper.OrdersBaseMapper;
import com.ovalm.reggie.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersBaseMapper, Orders> implements OrdersService {


    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    /**
     * 用户下单
     * @param orders
     */
    @Transactional
    @Override
    public void submit(Orders orders) {
        Long userId = BaseContext.getCurrentId();

        QueryWrapper<ShoppingCart> shoppingCartWrapper = new QueryWrapper<>();
        shoppingCartWrapper.eq("user_id", userId);
        List<ShoppingCart> cartList = shoppingCartService.list(shoppingCartWrapper);

        if(cartList == null || cartList.isEmpty()){
            throw new CustomException("购物车为空");
        }

        User user = userService.getById(userId);

        AddressBook address = addressBookService.getById(orders.getAddressBookId());
        if(address == null){
            throw new CustomException("地址信息有误");
        }

        long orderId = IdWorker.getId();

        AtomicInteger amount = new AtomicInteger(0);    // 保证原子操作
        List<OrderDetail> detailList = cartList.stream().map(item -> {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(item, detail);
            detail.setId(null);
            detail.setOrderId(orderId);
            int value = item.getAmount().multiply(new BigDecimal(item.getNumber())).intValue();
            amount.addAndGet(value);
            return detail;
        }).toList();

        // 设置订单信息然后保存
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));
        orders.setUserId(userId);
        orders.setNumber(String.valueOf(IdWorker.getId()));
        orders.setUserName(user.getName());
        orders.setConsignee(address.getConsignee());
        orders.setPhone(address.getPhone());
        String finalAddress =
                (address.getProvinceName() == null ? "" : address.getProvinceName())
                + (address.getCityName()  == null ? "" : address.getCityName())
                + (address.getDistrictName()  == null ? "" : address.getDistrictName())
                + address.getDetail();
        orders.setAddress(finalAddress);
        this.save(orders);

        // 保存订单详细信息
        orderDetailService.saveBatch(detailList);

        // 清空购物车
        shoppingCartService.remove(shoppingCartWrapper);

    }
}
