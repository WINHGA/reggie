package com.hga.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hga.reggie.entity.DishFlavor;
import com.hga.reggie.mapper.DishFlavorMapper;
import com.hga.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

/**
 * @Date 2023/6/11 18:55
 * @Author HGA
 * @Class DishFlavorServiceImpl
 * @Package com.hga.reggie.service.impl
 * Description: 菜品口味业务层
 */

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
