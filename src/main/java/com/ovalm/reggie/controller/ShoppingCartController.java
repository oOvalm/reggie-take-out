package com.ovalm.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ovalm.reggie.common.BaseContext;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.entity.ShoppingCart;
import com.ovalm.reggie.service.ShoppingCartService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.Query;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @GetMapping("/list")
    public R list() {
        String userId = BaseContext.getCurrentId().toString();
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId).orderByAsc("create_time");

        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.ok().put("data", list);
    }

    @PostMapping("/add")
    public R add(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);

//        if(shoppingCart.getDishFlavor() != null){
//            wrapper.eq("dish_flavor", shoppingCart.getDishFlavor());
//        }

        if(shoppingCart.getDishId() != null){   // 添加菜品
            wrapper.eq("dish_id", shoppingCart.getDishId());
        }
        else{       // 添加套餐
            wrapper.eq("setmeal_id", shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(wrapper);

        if(shoppingCartServiceOne != null){
            shoppingCartServiceOne.setNumber(shoppingCartServiceOne.getNumber()+1);
            shoppingCartService.updateById(shoppingCartServiceOne);
            return R.ok().put("data", shoppingCartServiceOne);
        }
        else{
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            return R.ok().put("data", shoppingCart);
        }
    }

    @PostMapping("/sub")
    public R sub(@RequestBody ShoppingCart shoppingCart) {
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        if(shoppingCart.getDishId() != null){
            wrapper.eq("dish_id", shoppingCart.getDishId());
        }
        else{
            wrapper.eq("setmeal_id", shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(wrapper);
        if(shoppingCartServiceOne.getNumber() == 1){
            shoppingCartService.removeById(shoppingCartServiceOne);
        }
        else{
            shoppingCartServiceOne.setNumber(shoppingCartServiceOne.getNumber()-1);
            shoppingCartService.updateById(shoppingCartServiceOne);
        }
        return R.ok().put("data", shoppingCartServiceOne);
    }

    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R clean(){
        Long userId = BaseContext.getCurrentId();
        QueryWrapper<ShoppingCart> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        shoppingCartService.remove(wrapper);
        return R.ok().put("data", "清空购物车成功");
    }


}
