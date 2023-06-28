package com.hga.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hga.reggie.common.CustomException;
import com.hga.reggie.common.R;
import com.hga.reggie.dto.DishDto;
import com.hga.reggie.entity.*;
import com.hga.reggie.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @Date 2023/6/11 18:57
 * @Author HGA
 * @Class DishController
 * @Package com.hga.reggie.controller
 * Description: 菜品管理
 */

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增菜品
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("添加的菜品信息："+ dishDto.toString());

        dishService.saveWithFlavor(dishDto);

        return R.success("新增菜品成功！");
    }

    /**
     *  菜品管理分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        // 构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        // 条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!=null,Dish::getName,name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);

        // 分页查询
        dishService.page(pageInfo,queryWrapper);

        // 对象拷贝（排除records属性不拷贝）
        BeanUtils.copyProperties(pageInfo,dishDtoPage,"records");

        // 原来的菜品信息
        List<Dish> records = pageInfo.getRecords();

        // 封装后的菜品信息
        List<DishDto> list = records.stream().map((item)->{

            DishDto dishDto = new DishDto();
            // 其他属性拷贝到DishDto对象中
            BeanUtils.copyProperties(item,dishDto);
            // 获取分类id
            Long categoryId = item.getCategoryId();
            // 通过分类id查询菜品分类信息
            Category category = categoryService.getById(categoryId);

            if(category != null){
                // 通过分类id查询 分类名称
                String categoryName = category.getName();
                // 将id设置到菜品中
                dishDto.setCategoryName(categoryName);
            }
            return dishDto;
        }).collect(Collectors.toList());

        // 设置分页中数据
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品信息 和对应的口味信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto byIdWithFlavor = dishService.getByIdWithFlavor(id);

        return R.success(byIdWithFlavor);
    }


    /**
     * 更新菜品
     * @param dishDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){
        log.info("修改的菜品信息："+ dishDto.toString());

        dishService.updateWithFlavor(dishDto);

        return R.success("修改菜品成功！");
    }


    /**
     *  根据条件查询对应的菜品数据
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        log.info("DishController:[list]:"+dish.toString());
        // 查询条件包装类
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        // 添加id 查询条件
        queryWrapper.eq(dish.getCategoryId()!= null, Dish::getCategoryId,dish.getCategoryId());
        // 添加起售条件
        queryWrapper.eq(Dish::getStatus,1);
        // 添加排序
        queryWrapper.orderByAsc(Dish::getCreateTime).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(queryWrapper);

        return R.success(list);
    }

    /**
     * 根据id删除菜品信息
     * @param ids
     * @return
     */
    @DeleteMapping
    @Transactional
    public R<String> removeWithDish(@RequestParam List<Long> ids){
        log.info("删除套餐的ids："+ids);

        // 1. 先判断是否有套餐关联了当前菜品
        for (Long id : ids) {

            // 查询当前id菜品
            Dish dish = dishService.getById(id);

            // 套餐查询条件
            LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SetmealDish::getDishId,id);
            // 查询当前菜品是否有套餐
            int count = setmealDishService.count(queryWrapper1);

            if(count > 0){
                throw new CustomException(dish.getName() +"有套餐关联，无法删除！");
            }

        }

        // 所有要删除都没有了套餐的关联，删除
        boolean b = dishService.removeByIds(ids);

        // 删除对应菜品口味
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(DishFlavor::getDishId,ids);

        dishFlavorService.remove(queryWrapper);

        return R.success("删除套餐成功！");
    }

    /**
     * 根据id修改销售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    @Transactional
    public R<String> updateOfStatus(@PathVariable int status, @RequestParam List<Long> ids){
        // 更新操作
        // 1. 先判断是否有套餐关联了当前菜品
        for (Long id : ids) {

            // 查询当前id菜品
            Dish dish = dishService.getById(id);

            if(dish.getStatus() == status){
                break;
            }

            // 套餐查询条件
            LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(SetmealDish::getDishId,id);
            // 查询当前菜品是否有套餐
//            SetmealDish setmealDish = setmealDishService.getOne(queryWrapper1);
            List<SetmealDish> list= setmealDishService.list(queryWrapper1);

            if(list.size() > 0){
                throw new CustomException(dish.getName() +"有"+ setmealService.getById(list.get(0).getSetmealId()).getName()+"关联，无法停售！");
            }

        }

        // 更新条件
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(Dish::getStatus,status);
        updateWrapper.in(Dish::getId,ids);
        // 更新状态
        dishService.update(updateWrapper);

        return R.success(status == 1? "起售成功":"停售成功");
    }



}
