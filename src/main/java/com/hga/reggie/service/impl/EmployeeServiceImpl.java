package com.hga.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hga.reggie.entity.Employee;
import com.hga.reggie.mapper.EmployeeMapper;
import com.hga.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * Date: 2023/5/3 15:32
 * Author: HGA
 * Class: EmployeeServiceImpl
 * Package:com.hga.reggie.service.impl
 * Description:
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
}
