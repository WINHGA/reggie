package com.hga.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.injector.methods.DeleteBatchByIds;
import com.baomidou.mybatisplus.core.injector.methods.DeleteById;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hga.reggie.common.CustomException;
import com.hga.reggie.dto.SetmealDto;
import com.hga.reggie.entity.Setmeal;
import com.hga.reggie.entity.SetmealDish;
import com.hga.reggie.mapper.SetmealMapper;
import com.hga.reggie.service.DishService;
import com.hga.reggie.service.SetmealDishService;
import com.hga.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Date: 2023/5/8 21:16
 * @Author: HGA
 * @Class: SetmealServiceImpl
 * @Package: com.hga.reggie.service.impl
 * Description: 套餐业务层
 */

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;
    /**
     * 新增套餐，同时需要保存套餐和菜品的关联信息
     * @param setmealDto
     */
    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        // 保存套餐的基本数据，操作setmeal，执行inset操作
        this.save(setmealDto);

        // 每一个菜品都应该保存对的套餐id
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 保存套餐和菜品的关联信息，操作setmeal_dish ,执行insert操作
        setmealDishService.saveBatch(setmealDto.getSetmealDishes());
    }

    /**
     * 根据 套餐id查询套餐信息和菜品信息
     * @param id
     * @return
     */
    @Override
    public SetmealDto getbyIdWithDish(Long id) {

        // 查询套餐信息
        Setmeal setmeal = this.getById(id);

        // 分装条件
        LambdaQueryWrapper<SetmealDish>  queryWrapper  = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        queryWrapper.orderByAsc(SetmealDish::getCreateTime).orderByDesc(SetmealDish::getUpdateTime);

        // 查询菜品信息
        List<SetmealDish> list = setmealDishService.list(queryWrapper);

        // 将套餐和菜品信息封装到SetmealDto中
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal,setmealDto);
        setmealDto.setSetmealDishes(list);


        return setmealDto;
    }

    /**
     *  更新套餐信息
     * @param setmealDto
     */
    @Transactional
    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        // 更新套餐信息
        this.updateById(setmealDto);

        // 删除setmeal_dish 表中有关当前套餐的菜品
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmealDto.getId() != null , SetmealDish::getSetmealId,setmealDto.getId());
        // 删除
        setmealDishService.remove(queryWrapper);

        // 重新添加信息的菜品信息
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map((item)->{
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());

        // 添加到setmeal_dish表中
        setmealDishService.saveBatch(setmealDishes);

    }

    /**
     * 根据id删除套餐
     * @param ids
     */
    @Override
    @Transactional
    public void removeWithDish(List<Long> ids) {

        // 查询是否能删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        // 对应的Selete * from setmeal where id in (2,3,4) and status = 1;
        queryWrapper.in(Setmeal::getId,ids);
        // 是否在销售中
        queryWrapper.eq(Setmeal::getStatus,1);

        if(this.count(queryWrapper) > 0){
            throw new CustomException("当前套餐在销售中，无法删除");
        }

        // 删除套餐
        this.removeByIds(ids);

        // 删除菜品信息的条件
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);

        // 删除对应的套餐的菜品信息。
        setmealDishService.remove(queryWrapper1);

    }
}
