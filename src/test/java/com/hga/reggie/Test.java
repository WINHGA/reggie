package com.hga.reggie;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

/**
 * @Date 2023/6/10 22:43
 * @Author HGA
 * @Class com.hga.reggie.Test
 * @Package PACKAGE_NAME
 * Description:
 */


@SpringBootTest
@ContextConfiguration(classes = ReggieApplication.class)
public class Test {

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now());
    }
}
