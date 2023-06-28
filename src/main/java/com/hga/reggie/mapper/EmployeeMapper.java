package com.hga.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hga.reggie.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @date: 2023/5/3 15:14
 * @author: HGA
 * @class: EmployeeMapper
 * Description:
 */

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
