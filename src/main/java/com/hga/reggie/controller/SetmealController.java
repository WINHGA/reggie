package com.hga.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hga.reggie.common.R;
import com.hga.reggie.dto.SetmealDto;
import com.hga.reggie.entity.Category;
import com.hga.reggie.entity.Setmeal;
import com.hga.reggie.entity.SetmealDish;
import com.hga.reggie.service.CategoryService;
import com.hga.reggie.service.SetmealDishService;
import com.hga.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

/**
 * @Date 2023/6/12 15:18
 * @Author HGA
 * @Class SetmealController
 * @Package com.hga.reggie.controller
 * Description:
 */

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealService setmealService;

    /**
     * 新增套餐信息
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("套餐信息：{}",setmealDto.toString());

        // 保存信息
        setmealService.saveWithDish(setmealDto);

        return R.success("新增套餐成功！");
    }

    /**
     * 套餐分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page<SetmealDto>> page(int page, int pageSize, @Param("name")String name){

        // 分页构造器
        Page<Setmeal> pageInof = new Page<>(page,pageSize);
        Page<SetmealDto> pages = new Page<>();

        // 添加条件
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(name!= null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        // 查询
        setmealService.page(pageInof,queryWrapper);

        // 将查询的条件赋值到 pages 中，排除records
        BeanUtils.copyProperties(pageInof,pages,"records");

        // 获取pageInfo中的records
        List<Setmeal> records = pageInof.getRecords();

        List<SetmealDto> list = records.stream().map((item)->{
            SetmealDto setmealDto = new SetmealDto();

            // 将基本数据都赋值到setmealDto中
            BeanUtils.copyProperties(item,setmealDto);

            // 获取item中的套餐分类的id
            Long categoryId = item.getCategoryId();

            Category category = categoryService.getById(categoryId);
            if(category!= null){
                // 通过套餐id 获取 套餐名
                String name1 = category.getName();

                // 设置SetmealDto中的套餐名
                setmealDto.setCategoryName(name1);

            }

            return setmealDto;
        }).collect(Collectors.toList());

        // 将数据设置到pages中的records中
        pages.setRecords(list);

        return R.success(pages);
    }

    /**
     * 根据id查询套餐信息，和套餐对应的菜品
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> get(@PathVariable Long id){
        log.info("根据id查询套餐信息："+ id);

        SetmealDto setmealDto = setmealService.getbyIdWithDish(id);

        return R.success(setmealDto);
    }


    /**
     * 更新套餐信息
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto){
        log.info("更新套餐信息：{}",setmealDto.toString());

        // 保存信息
        setmealService.updateWithDish(setmealDto);

        return R.success("更新套餐成功！");
    }

    /**
     * 根据id删除套餐信息
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("删除套餐的ids："+ids);

        setmealService.removeWithDish(ids);

        return R.success("删除套餐成功！");
    }

    /**
     * 更新起售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> updateOfStatus(@PathVariable int status,@RequestParam List<Long> ids){
        log.info("销售状态："+status);

        // 设置更新条件
        LambdaUpdateWrapper<Setmeal> updateWrapper = new LambdaUpdateWrapper<>();
        // 设置要更改的值
        updateWrapper.set(Setmeal::getStatus,status);
        // 设置条件
        updateWrapper.in(Setmeal::getId,ids);
        // 执行更新
        setmealService.update(updateWrapper);

        return R.success(status == 0 ? "停售成功！":"起售成功！");
    }
}
