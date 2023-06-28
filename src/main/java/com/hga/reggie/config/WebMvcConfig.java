package com.hga.reggie.config;

import ch.qos.logback.classic.pattern.MessageConverter;
import com.hga.reggie.common.JacksonObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * @date: 2023/5/3 14:22
 * @author: HGA
 * @class: WebMvcConfig
 * Description:
 */
@Slf4j
@Configuration
public class WebMvcConfig extends WebMvcConfigurationSupport {
    /**
     * 设置静态资源映射
     * @param registry
     */
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        log.info("开始静态资源映射...");
        registry.addResourceHandler("/front/**").addResourceLocations("classpath:/front/");
        registry.addResourceHandler("/backend/**").addResourceLocations("classpath:/backend/");
    }


    /**
     * 扩展mvc框架的消息转换器
     *
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters){
        // 创建消息转换器对象
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        // 设置对象转换器，底层使用Jackson将Java对象转换成json
        messageConverter.setObjectMapper(new JacksonObjectMapper());

        // 将上面的消息转换器对象追加到mvc框架的转换器集合中
        converters.add(0,messageConverter);
    }
}
