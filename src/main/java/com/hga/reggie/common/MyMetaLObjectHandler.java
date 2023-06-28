package com.hga.reggie.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

/**
 * @Date: 2023/5/4 16:23
 * @Author: HGA
 * @Class: MyMetaLObjectHandler
 * @Package: com.hga.reggie.common
 * Description: 自定义元数据对象处理器
 */

@Component
@Slf4j
public class MyMetaLObjectHandler implements MetaObjectHandler {

//    @Autowired
//    private HttpSession httpSession;

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("公共字段自动填充:[insertFill]");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        // 从封装类中获取数据。
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
        // 也可以使用这种方式从 session 域中获取值
        // metaObject.setValue("updateUser", httpSession.getAttribute("employee"));
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("公共字段自动填充:[updateFill]");
        log.info(metaObject.toString());
        metaObject.setValue("updateUser", BaseContext.getCurrentId());
        metaObject.setValue("updateTime", LocalDateTime.now());
    }
}
