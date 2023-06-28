package com.hga.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hga.reggie.common.CustomException;
import com.hga.reggie.entity.Category;
import com.hga.reggie.entity.Dish;
import com.hga.reggie.entity.Setmeal;
import com.hga.reggie.mapper.CategoryMapper;
import com.hga.reggie.service.CategoryService;
import com.hga.reggie.service.DishService;
import com.hga.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.prefs.BackingStoreException;

/**
 * @Date: 2023/5/4 17:28
 * @Author: HGA
 * @Class: CategoryServiceImpl
 * @Package: com.hga.reggie.service.impl
 * Description: 分类业务层实体类
 */

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper,Category> implements CategoryService{

    // 菜品 Service 方法
    @Autowired
    private DishService dishService;

    // 套餐 Service 方法
    @Autowired
    private SetmealService setmealService;

    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id){
        // 查询当前分类是否关联了菜品，如果已经关联了菜品，抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 添加查询条件,根据id 查询
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,id);

        int count = dishService.count(dishLambdaQueryWrapper);
        if(count > 0){
            // 抛出异常，说明有菜品关联了这个分类。
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }
        // 查询当前分类是否关联了套餐，如果关联了，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);

        int count1 = setmealService.count(setmealLambdaQueryWrapper);
        if(count1 > 0){
            // 抛出异常，说明有套餐关联了这个分类。
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }
        // 正常删除分类
        super.removeById(id);
    }
}
