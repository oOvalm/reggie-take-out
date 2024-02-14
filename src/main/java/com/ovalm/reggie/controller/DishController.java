package com.ovalm.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.dto.DishDto;
import com.ovalm.reggie.entity.Category;
import com.ovalm.reggie.entity.Dish;
import com.ovalm.reggie.entity.DishFlavor;
import com.ovalm.reggie.entity.Employee;
import com.ovalm.reggie.service.CategoryService;
import com.ovalm.reggie.service.DishFlavorService;
import com.ovalm.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

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

        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>(page, pageSize);

        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        if(!StringUtils.isNullOrEmpty(name)) {
            wrapper.like("name", name);
        }
        wrapper.orderByDesc("update_time");

        dishService.page(pageInfo, wrapper);

        // 对象拷贝
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");

        // 查表查分类名称转成DishDto
        List<DishDto> list = pageInfo.getRecords().stream().map(item -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if(category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).toList();

        dishDtoPage.setRecords(list);

        return R.ok().put("data", dishDtoPage);
    }

    /**
     * 新增菜品
     * @param dto
     * @return
     */
    @PostMapping
    public R save(@RequestBody DishDto dto){
        dishService.saveWithFlavor(dto);
        // 精确清理分类id
        String key = "dish::" + dto.getCategoryId() + "_" + dto.getStatus();
        redisTemplate.delete(key);
        return R.ok();
    }

    /**
     * 根据菜品id查菜品
     * @param dishId
     * @return
     */
    @GetMapping("/{dishId}")
    public R info(@PathVariable("dishId") Long dishId){
        DishDto dto = dishService.getByIdWithFlavor(dishId);
        return R.ok().put("data", dto);
    }

    /**
     * 更新菜品
     * @param dto
     * @return
     */
    @PutMapping
    public R update(@RequestBody DishDto dto){
        dishService.updateWithFlavor(dto);
        Set<Object> keys = redisTemplate.keys("dish::*");
        redisTemplate.delete(keys);
        return R.ok();
    }

    /**
     * 修改菜品状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R changeStatus(@PathVariable("status") Integer status, Long[] ids){
        List<Long> list = Arrays.stream(ids).toList();
        dishService.changeStatus(list, status);
        return R.ok();
    }

    /**
     * 删除菜品
     * @param ids
     * @return
     */
    @DeleteMapping
    public R delete(Long[] ids){
        dishService.deleteIds(ids);

        return R.ok();
    }

    /**
     * 根据分类Id查询该分类下的菜品
     * @param dish
     * @return
     */
//    @GetMapping("/list")
//    public R list(Dish dish){
//        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
//        if(dish.getCategoryId() != null){
//            wrapper.eq("category_id", dish.getCategoryId());
//        }
//        wrapper.eq("status", 1L);
//        wrapper.orderByAsc("sort").orderByDesc("update_time");
//        List<Dish> list = dishService.list(wrapper);
//        return R.ok().put("data", list);
//    }



    @GetMapping("/list")
    public R list(Dish dish){
        String key = "dish::" + dish.getCategoryId() + "_" + dish.getStatus();
        List<DishDto> dtos = (List<DishDto>) redisTemplate.opsForValue().get(key);
        if(dtos != null){
            return R.ok().put("data", dtos);
        }

        QueryWrapper<Dish> wrapper = new QueryWrapper<>();
        if(dish.getCategoryId() != null){
            wrapper.eq("category_id", dish.getCategoryId());
        }
        wrapper.eq("status", 1L);
        wrapper.orderByAsc("sort").orderByDesc("update_time");
        List<DishDto> list = dishService.list(wrapper).stream().map(item -> {
            DishDto dto = new DishDto();
            BeanUtils.copyProperties(item, dto);

            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                dto.setCategoryName(category.getName());
            }

            QueryWrapper<DishFlavor> wr = new QueryWrapper<>();
            wr.eq("dish_id", dto.getId());
            dto.setFlavors(dishFlavorService.list(wr));
            return dto;
        }).toList();

        redisTemplate.opsForValue().set(key, list, 30, TimeUnit.MINUTES);
        return R.ok().put("data", list);
    }
}
