package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.dto.DishDto;
import com.ovalm.reggie.entity.Dish;
import com.ovalm.reggie.entity.DishFlavor;
import com.ovalm.reggie.mapper.DishBaseMapper;
import com.ovalm.reggie.service.DishFlavorService;
import com.ovalm.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;


@Service
public class DishServiceImpl extends ServiceImpl<DishBaseMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Transactional
    @Override
    public void saveWithFlavor(DishDto dto) {
        this.save(dto);
        Long dishId = dto.getId();
        List<DishFlavor> list = dto.getFlavors().stream().peek(item -> item.setDishId(dishId)).toList();
        dishFlavorService.saveBatch(list);
    }

    @Override
    public DishDto getByIdWithFlavor(Long dishId) {
        DishDto dto = new DishDto();

        Dish dish = this.getById(dishId);
        BeanUtils.copyProperties(dish, dto);

        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.eq("dish_id", dish.getId());
        List<DishFlavor> list = dishFlavorService.list(wrapper);
        dto.setFlavors(list);
        return dto;
    }

    @Override
    public void updateWithFlavor(DishDto dto) {
        this.updateById(dto);

        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.eq("dish_id", dto.getId());
        dishFlavorService.remove(wrapper);

        List<DishFlavor> list = dto.getFlavors().stream().peek(item -> item.setDishId(dto.getId())).toList();

        dishFlavorService.saveBatch(list);
    }

    @Override
    public void changeStatus(List<Long> ids, Integer status) {
        Dish dish = new Dish();
        for (Long id : ids) {
            dish.setId(id);
            dish.setStatus(status);
            this.updateById(dish);

        }
    }

    @Transactional
    @Override
    public void deleteIds(Long[] ids){
        List<Long> list = Arrays.stream(ids).toList();
        this.removeBatchByIds(list);
        QueryWrapper<DishFlavor> wrapper = new QueryWrapper<>();
        wrapper.in("dish_id", list);
        dishFlavorService.remove(wrapper);
    }
}
