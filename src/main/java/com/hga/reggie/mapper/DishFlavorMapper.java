package com.hga.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hga.reggie.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date 2023/6/11 18:52
 * @Author HGA
 * @Class DishFlavorMapper
 * @Package com.hga.reggie.mapper
 * Description: 菜品口味持久层
 */

@Mapper
public interface DishFlavorMapper extends BaseMapper<DishFlavor> {
}
