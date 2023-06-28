package com.hga.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * @Date: 2023/5/3 21:00
 * @Author: HGA
 * @Class: GlobalExceptionHandler
 * @Package: com.hga.reggie.common
 * Description: 全局异常捕获处理
 *
 * 使用 spring 中 AOP 切面思想，给所有的添加了
 * RestController.class, Controller.class 注解的类，添加捕获异常方法
 */

@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {

    /**
     *
     * 捕获异常 SQLIntegrityConstraintViolationException，进行处理
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException exception){
        // 日志打印
        log.info(exception.getMessage());

        // 进行异常逻辑处理
        if(exception.getMessage().contains("Duplicate entry")){
            // 异常信息 ： Duplicate entry 'zhangsan' for key 'employee.idx_username'

            String[] split = exception.getMessage().split(" ");

            String msg = split[2] + "已存在！";

            return R.error(msg);
        }

        // 消息,默认消息
        return R.error("未知错误");
    }

    /**
     *
     * 捕获异常 CustomerException，进行处理
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException exception){
        // 日志打印
        log.info(exception.getMessage());

        // 消息,默认消息
        return R.error(exception.getMessage());
    }

}
