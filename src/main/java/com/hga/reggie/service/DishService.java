package com.hga.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hga.reggie.dto.DishDto;
import com.hga.reggie.entity.Dish;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Date: 2023/5/8 21:12
 * @Author: HGA
 * @Class: DishService
 * @Package: com.hga.reggie.service
 * Description: 菜品业务层
 */

public interface DishService extends IService<Dish> {
    // 新增菜品，同时保存对应的口味数据
    public void saveWithFlavor(DishDto dishDto);

    // 获取菜品信息，同时获取口味信息
    public DishDto getByIdWithFlavor(Long id);

    // 更新菜品信息
    public void updateWithFlavor(DishDto dishDto);

//    // 根据id删除菜品
//    public void removeWithDish(List<Long> ids);
}
