package com.ovalm.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mysql.cj.util.StringUtils;
import com.ovalm.reggie.common.R;
import com.ovalm.reggie.entity.Category;
import com.ovalm.reggie.entity.Employee;
import com.ovalm.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@Slf4j
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增分类
     * @param category
     * @return
     */
    @PostMapping
    public R save(@RequestBody Category category){
        System.out.println(category);
        categoryService.save(category);
        log.info("新增分类");
        return R.ok();
    }

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R listPage(int page, int pageSize){
        Page<Category> pageInfo = new Page<>(page, pageSize);

        QueryWrapper<Category> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("update_time");

        categoryService.page(pageInfo, wrapper);
        return R.ok().put("data", pageInfo);
    }


    /**
     * 删除菜品
     */
    @DeleteMapping
    public R delete(Long ids){
        log.info("删除分类 {}", ids);
        categoryService.remove(ids);
        return R.ok();
    }


    /**
     * 根据id修改分类信息
     */
    @PutMapping
    public R update(@RequestBody Category category){
        log.info("修改分类信息: {}", category);
        categoryService.updateById(category);
        return R.ok();
    }


    /**
     * 查询所有分类
     * type: 1、2
     */
    @GetMapping("/list")
    public R list(Category category) {

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if(category != null && category.getType() != null) {
            wrapper.eq(Category::getType, category.getType());
        }
        wrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = categoryService.list(wrapper);

        return R.ok().put("data", list);
    }



}
