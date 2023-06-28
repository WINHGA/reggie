package com.hga.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hga.reggie.entity.Category;

/**
 * @Date: 2023/5/4 17:28
 * @Author: HGA
 * @Class: CategoryService
 * @Package: com.hga.reggie.service
 * Description: 分类业务层
 */
public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
