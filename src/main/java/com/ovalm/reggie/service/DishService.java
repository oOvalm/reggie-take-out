package com.ovalm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ovalm.reggie.dto.DishDto;
import com.ovalm.reggie.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dto);

    DishDto getByIdWithFlavor(Long dishId);

    void updateWithFlavor(DishDto dto);

    void changeStatus(List<Long> ids, Integer status);

    void deleteIds(Long[] ids);
}
