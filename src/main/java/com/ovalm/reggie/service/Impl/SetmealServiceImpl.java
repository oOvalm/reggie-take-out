package com.ovalm.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ovalm.reggie.common.CustomException;
import com.ovalm.reggie.dto.SetmealDto;
import com.ovalm.reggie.entity.Dish;
import com.ovalm.reggie.entity.Setmeal;
import com.ovalm.reggie.entity.SetmealDish;
import com.ovalm.reggie.mapper.SetmealBaseMapper;
import com.ovalm.reggie.service.SetmealDishService;
import com.ovalm.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealBaseMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Transactional
    @Override
    public void saveWithSetmealDish(SetmealDto setmealDto) {
        this.save(setmealDto);

        List<SetmealDish> list = setmealDto.getSetmealDishes().stream().peek(item -> item.setSetmealId(setmealDto.getId())).toList();

        setmealDishService.saveBatch(list);
    }

    @Override
    public SetmealDto getSetmealDtoById(Long setmealId) {
        SetmealDto dto = new SetmealDto();
        Setmeal setmeal = this.getById(setmealId);
        BeanUtils.copyProperties(setmeal, dto);

        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.eq("setmeal_id", setmealId);
        List<SetmealDish> list = setmealDishService.list(wrapper);

        dto.setSetmealDishes(list);
        return dto;
    }

    @Transactional
    @Override
    public void update(SetmealDto setmealDto) {
        this.updateById(setmealDto);

        QueryWrapper<SetmealDish> wrapper = new QueryWrapper<>();
        wrapper.eq("setmeal_id", setmealDto.getId());
        setmealDishService.remove(wrapper);

        List<SetmealDish> list = setmealDto.getSetmealDishes().stream().peek(item -> item.setSetmealId(setmealDto.getId())).toList();

        setmealDishService.saveBatch(list);
    }

    @Override
    public void changeStatus(List<Long> list, Integer status) {
        Setmeal setmeal = new Setmeal();
        for (Long id : list) {
            setmeal.setId(id);
            setmeal.setStatus(status);
            this.updateById(setmeal);

        }
    }

    @Transactional
    @Override
    public void deleteIds(Long[] ids){
        List<Long> list = Arrays.stream(ids).toList();
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        wrapper.in("id", list).eq("status", 1L);
        long count = this.count(wrapper);
        if(count > 0){
            throw new CustomException("有正在销售的套餐，删除操作失败");
        }

        this.removeByIds(list);
        QueryWrapper<SetmealDish> wrapper1 = new QueryWrapper<>();
        wrapper1.in("setmeal_id", list);
        setmealDishService.remove(wrapper1);
    }
}
