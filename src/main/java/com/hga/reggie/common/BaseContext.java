package com.hga.reggie.common;

/**
 * @Date: 2023/5/4 16:53
 * @Author: HGA
 * @Class: BaseContext
 * @Package: com.hga.reggie.common
 * Description:  基于 ThreadLocal 封装工具类，用户保存和获取当前登录用户id
 */

public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }

    public static Long getCurrentId(){
        return threadLocal.get();
    }
}
