package com.hga.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hga.reggie.entity.Setmeal;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date: 2023/5/8 21:10
 * @Author: HGA
 * @Class: SetmealMapper
 * @Package: com.hga.reggie.mapper
 * Description: 套餐持久层
 */

@Mapper
public interface SetmealMapper extends BaseMapper<Setmeal> {
}
