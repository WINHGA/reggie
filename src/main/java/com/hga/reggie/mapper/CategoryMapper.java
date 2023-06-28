package com.hga.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hga.reggie.entity.Category;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Date: 2023/5/4 17:13
 * @Author: HGA
 * @Class: CategoryMapper
 * @Package: com.hga.reggie.mapper
 * Description:  分类持久层
 */

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
