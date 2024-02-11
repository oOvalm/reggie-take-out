package com.ovalm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ovalm.reggie.dto.SetmealDto;
import com.ovalm.reggie.entity.Setmeal;
import com.ovalm.reggie.mapper.SetmealBaseMapper;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    void saveWithSetmealDish(SetmealDto setmealDto);

    SetmealDto getSetmealDtoById(Long setmealId);

    void update(SetmealDto setmealDto);

    void changeStatus(List<Long> list, Integer status);

    void deleteIds(Long[] ids);
}
