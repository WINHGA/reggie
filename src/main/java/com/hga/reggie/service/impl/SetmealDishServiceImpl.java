package com.hga.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hga.reggie.entity.SetmealDish;
import com.hga.reggie.mapper.SetmealDishMapper;
import com.hga.reggie.service.SetmealDishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

/**
 * @Date 2023/6/12 15:16
 * @Author HGA
 * @Class SetmealDishServiceImpl
 * @Package com.hga.reggie.service.impl
 * Description:
 */

@Service
@Slf4j
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
