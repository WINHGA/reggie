package com.hga.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hga.reggie.entity.SetmealDish;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date 2023/6/12 15:14
 * @Author HGA
 * @Class SetmealDishMapper
 * @Package com.hga.reggie.mapper
 * Description:
 */

@Mapper
public interface SetmealDishMapper extends BaseMapper<SetmealDish> {
}
