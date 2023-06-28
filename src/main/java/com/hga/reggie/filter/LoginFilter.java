package com.hga.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.hga.reggie.common.BaseContext;
import com.hga.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Date: 2023/5/3 17:21
 * @Author: HGA
 * @Class: LoginFilter
 * @Package:  com.hga.reggie.filter
 * Description: 检查用户是否完成登录
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginFilter implements Filter {

    // 路径匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    /*
        1. 获取本次请求的 URI
        2. 判断本次请求是否需要处理
        3. 如果不需要处理，则直接放行
        4. 判断登录状态，如果已登录，则直接放行
        5. 如果未登录则返回未登录结果
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // 1. 获取本次请求的 URI
        String uri = request.getRequestURI();
        log.info("拦截到请求：{}",uri);

        // 2. 判断本次请求是否需要处理
        boolean check = check(uri);

        // 3. 如果不需要处理，则直接放行
        if(check){
            log.info("本次请求{}不需要处理",uri);
            filterChain.doFilter(request,response);
            return;
        }

        // 4. 判断登录状态，如果已登录，则直接放行
        if(request.getSession().getAttribute("employee") != null){
            log.info("用户已登录，用户id为：{}",request.getSession().getAttribute("employee"));
            Long empId = (Long) request.getSession().getAttribute("employee");
            // 将当前登录用户的id设置到线程锁中，只要是同一个线程，数据就不变。
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }

        log.info("用户未登录");

        // 5. 如果未登录则返回未登录结果,通过输出流的方式向浏览器响应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
    }

    /**
     * 路径匹配，判断是否需要请求拦截
     * @param requestURI
     * @return
     */
    public boolean check(String requestURI){
        // 不需要拦截的请求
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**"
        };

        for (String url : urls) {
            // 匹配是否包含
            if(PATH_MATCHER.match(url,requestURI)){
                return true;
            }
        }
        // 到这，就说明没有一个匹配上的，说明请求路径需要拦截
        return false;
    }
}
