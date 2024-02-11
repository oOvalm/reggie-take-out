package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.common.CustomException;
import com.ovalm.reggie.entity.Category;
import com.ovalm.reggie.entity.Dish;
import com.ovalm.reggie.entity.Setmeal;
import com.ovalm.reggie.mapper.CategoryBaseMapper;
import com.ovalm.reggie.service.CategoryService;
import com.ovalm.reggie.service.DishService;
import com.ovalm.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryBaseMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;

    @Override
    public void remove(Long ids) {
        Category category = this.getById(ids);
        if(category.getType() == 1){    // 菜品分类
            long count = dishService.count(new QueryWrapper<Dish>().eq("category_id", ids));
            if(count > 0){
                throw new CustomException("当前分类下关联了菜品，不能删除！");
            }
        }
        else{   // 套餐分类
            long count = setmealService.count(new QueryWrapper<Setmeal>().eq("category_id", ids));
            if(count > 0){
                throw new CustomException("当前分类下关联了套餐，不能删除！");
            }
        }
        this.removeById(ids);

    }
}
