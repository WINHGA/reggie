package com.hga.reggie.common;

/**
 * @Date: 2023/5/8 21:42
 * @Author: HGA
 * @Class: CustomException
 * @Package: com.hga.reggie.common
 * Description: 自定义业务异常类
 */

public class CustomException extends RuntimeException{

    public CustomException(String message){
        super(message);
    }
}
