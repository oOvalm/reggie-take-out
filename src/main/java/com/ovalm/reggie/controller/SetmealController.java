package com.ovalm.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.ovalm.reggie.common.CustomException;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.dto.SetmealDto;
import com.ovalm.reggie.entity.*;
import com.ovalm.reggie.service.CategoryService;
import com.ovalm.reggie.service.SetmealDishService;
import com.ovalm.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询菜品
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R listPage(int page, int pageSize, String name){

        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>(page, pageSize);

        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderByDesc("update_time");

        setmealService.page(pageInfo, wrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, setmealDtoPage, "records");

        // 查表查分类名称转成DishDto
        List<SetmealDto> list = pageInfo.getRecords().stream().map(item -> {
            SetmealDto dishDto = new SetmealDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).toList();

        setmealDtoPage.setRecords(list);

        return R.ok().put("data", setmealDtoPage);
    }

    /**
     * 保存套餐和对应的菜品关系
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R save(@RequestBody SetmealDto setmealDto){
        setmealService.saveWithSetmealDish(setmealDto);
        return R.ok();
    }

    /**
     * 获取套餐信息
     * @param setmealId
     * @return
     */
    @GetMapping("/{setmealId}")
    public R info(@PathVariable("setmealId") Long setmealId){
        SetmealDto dto = setmealService.getSetmealDtoById(setmealId);
        return R.ok().put("data", dto);
    }

    @PutMapping
    public R update(@RequestBody SetmealDto setmealDto){
        setmealService.update(setmealDto);
        return R.ok();
    }


    /**
     * 修改套餐状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R changeStatus(@PathVariable("status") Integer status, Long[] ids){
        List<Long> list = Arrays.stream(ids).toList();
        setmealService.changeStatus(list, status);
        return R.ok();
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(Long[] ids){
        setmealService.deleteIds(ids);

        return R.ok();
    }


    /**
     * 根据分类Id查询该套餐下的菜品
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public R list(Setmeal setmeal){
        QueryWrapper<Setmeal> wrapper = new QueryWrapper<>();
        if(setmeal.getCategoryId() != null){
            wrapper.eq("category_id", setmeal.getCategoryId());
        }
        wrapper.eq("status", 1L);
        wrapper.orderByDesc("update_time");
        List<Setmeal> list = setmealService.list(wrapper);
        return R.ok().put("data", list);
    }
}
