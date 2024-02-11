package com.ovalm.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ovalm.reggie.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long ids);
}
