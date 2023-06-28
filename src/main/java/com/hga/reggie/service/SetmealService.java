package com.hga.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hga.reggie.dto.SetmealDto;
import com.hga.reggie.entity.Setmeal;

import java.util.List;

/**
 * @Date: 2023/5/8 21:14
 * @Author: HGA
 * @Class: SetmealService
 * @Package: com.hga.reggie.service
 * Description:  套餐业务层
 */

public interface SetmealService extends IService<Setmeal> {

    /**
     * 新增套餐，同时需要保存套餐和菜品的关联信息
     * @param setmealDto
     */
    public void saveWithDish(SetmealDto setmealDto);

    /**
     *  根据 套餐id查询套餐信息和菜品信息
     * @param id
     * @return
     */
    public SetmealDto getbyIdWithDish(Long id);

    /**
     * 更新套餐，同时需要保存套餐和菜品的关联信息
     * @param setmealDto
     */
    public void updateWithDish(SetmealDto setmealDto);

    /**
     * 根据id删除套餐
     * @param ids
     */
    public void removeWithDish(List<Long> ids);

}
