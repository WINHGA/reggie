package com.hga.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hga.reggie.common.R;
import com.hga.reggie.entity.Employee;
import com.hga.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Date: 2023/5/3 15:36
 * @Author: HGA
 * Class: EmployeeController
 * Package:com.hga.reggie.controller
 * Description:  控制层，
 */

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    // 服务层的对象
    @Autowired
    private EmployeeService employeeService;

    /*
        员工登录功能控制方法：
        1. 将页面提交过来的密码 password 进行 md 加密处理
        2. 根据页面提交的用户名 username 查询数据库
        3. 如果没有查询到则返回登录失败
        4. 密码对比，如果不一致则返回登录失败结果
        5. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        6. 登录成功，将员工 id 存入到 Session 域并返回登录成功结果
     */
    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){

        // 1. 将页面提交过来的密码 password 进行 md 加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        // 2. 根据页面提交的用户名 username 查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);

        // 3. 如果没有查询到则返回登录失败
        if(emp == null){
            return R.error("登录失败");
        }

        // 4. 密码对比，如果不一致则返回登录失败结果
        if(!emp.getPassword().equals(password)){
            return R.error("密码错误");
        }

        // 5. 查看员工状态，如果为已禁用状态，则返回员工已禁用结果 1:正常 0: 已禁用
        if(emp.getStatus() == 0){
            return R.error("账号已禁用");
        }

        // 6. 登录成功，将员工 id 存入到 Session 域并返回登录成功结果
        request.getSession().setAttribute("employee",emp.getId());

        return R.success(emp);
    }


    /*
        员工退出功能控制方法：
        1. 清除 Session 中保存的当前登录员工的 id
     */
    @PostMapping("/logout")
    public R<String> logout(HttpServletRequest request){
        // 1. 清除 Session 中保存的当前登录员工的 id
        request.removeAttribute("employee");
        return R.success("退出成功");
    }

    /*
        新增员工功能控制方法：
        1. 采用 restful 风格，请求方式加请求路径。
        2. PostMapping  post 请求，请求路径不设置，就是父路径
        3. 添加员工是没有密码的， 设置初始密码，并且初始密码，需要加密，保证数据库中统一
        4. 为员工的其他属性赋值。
        5. 提交到 service 层 添加员工
     */
    @PostMapping
    public R<String> save(HttpServletRequest request,@RequestBody Employee employee){
        log.info("新增员工："+employee.toString());

        // 设置初始密码123456，需要进行md5加密。
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        // 设置添加时间
        // employee.setCreateTime(LocalDateTime.now());
        // 设置更新时间
        // employee.setUpdateTime(LocalDateTime.now());

        // 获取登陆的用户 id
        Long empId = (Long) request.getSession().getAttribute("employee");

        // 设置操作对象的id
        // employee.setCreateUser(empId);
        // 设置更新对象的id
        // employee.setUpdateUser(empId);

        // 添加到数据库
        employeeService.save(employee);

        return R.success("添加成功");
    }

    /**
     * 员工信息分页查询
     * @param page 第几页
     * @param pageSize 每页多少
     * @param name 查询内容关键字
     * @return
     */
    @GetMapping("/page")
    public R<Page<Employee>> page(int page,int pageSize,String name){
        log.info("page = {},pageSize = {} ,name = {}",page,pageSize,name);

        // 构造分页构造器
        Page<Employee> pageInfo = new Page<>(page,pageSize);

        // 构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name);
        // 添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        // queryWrapper.orderByAsc(Employee::getUpdateTime);

        // 执行查询
        employeeService.page(pageInfo,queryWrapper);

        return R.success(pageInfo);
    }

    /**
     * 根据员工的id 修改员工信息
     * @param employee
     * @return
     */
    @PutMapping
    public R<String> update(HttpServletRequest request,@RequestBody Employee employee){
        log.info("更新用户的信息："+employee.toString());

        // 设置修改时间
        // employee.setUpdateTime(LocalDateTime.now());

        // 设置修改人
        // employee.setUpdateUser((Long)request.getSession().getAttribute("employee"));

        // 执行更新
        employeeService.updateById(employee);

        return R.success("更新成功！");
    }

    /**
     * 根据id查询员工信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<Employee> getById(@PathVariable Long id){
        log.info("根据id查询员工信息...");
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("没有查询到对应的员工信息...");
    }


}
