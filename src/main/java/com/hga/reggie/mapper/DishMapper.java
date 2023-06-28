package com.hga.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hga.reggie.dto.DishDto;
import com.hga.reggie.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date: 2023/5/8 21:09
 * @Author: HGA
 * @Class: DishMapper
 * @Package: com.hga.reggie.mapper
 * Description: 菜品持久层
 */

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}
