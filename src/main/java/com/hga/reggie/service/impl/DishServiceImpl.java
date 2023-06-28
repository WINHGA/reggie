package com.hga.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hga.reggie.dto.DishDto;
import com.hga.reggie.entity.Dish;
import com.hga.reggie.entity.DishFlavor;
import com.hga.reggie.mapper.DishMapper;
import com.hga.reggie.service.DishFlavorService;
import com.hga.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date: 2023/5/8 21:14
 * @Author: HGA
 * @Class: DishServiceImpl
 * @Package: com.hga.reggie.service.impl
 * Description:  菜品业务层
 */

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        // 保存菜品
        this.save(dishDto);

        Long dishId = dishDto.getId();// 菜品id

        // 菜品口味
        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList = flavorList.stream().map((item) -> {
            item.setDishId(dishId);
            return item;
        }).collect(Collectors.toList());

        // 保存口味
        dishFlavorService.saveBatch(flavorList);
    }

    /**
     *  根据菜品id查询菜品信息和口味信息
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {

        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);

        // 根据id查询对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(id != null,DishFlavor::getDishId,id);

        // 查询口味信息
        List<DishFlavor> flavorList = dishFlavorService.list(queryWrapper);

        // 将信息复制到DishDto中
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(flavorList);

        return dishDto;
    }


    /**
     * 更新菜品信息
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新菜品
        this.updateById(dishDto);

        // 先删除表中的口味信息
        LambdaQueryWrapper<DishFlavor> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(DishFlavor::getDishId,dishDto.getId());

        dishFlavorService.remove(lambdaQueryWrapper);

        // 更新口味信息
        List<DishFlavor> flavorList = dishDto.getFlavors();
        flavorList = flavorList.stream().map((item) -> {
            item.setDishId(dishDto.getId());
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(flavorList);

    }

    /**
     * 根据菜品id删除菜品信息
     * @param ids
     */
//    @Override
//    public void removeWithDish(List<Long> ids) {
//        // 1. 判断是否有套餐在使用该菜品
//
//    }
}
