package com.ovalm.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.ovalm.reggie.common.BaseContext;
import com.ovalm.reggie.common.R;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;

/**
 * 检查用户是否登录
 */
@Slf4j
@WebFilter(filterName = "LoginCheckFilter", urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    public static String[] URLS = new String[]{
            "/employee/login",
            "/user/login",
            "/user/sendMsg",
            "/backend/**",
            "/front/**",
    };
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        long id = Thread.currentThread().getId();
        log.info("线程Id: {}", id);
        log.info("拦截请求  " + request.getRequestURI());
        String requestURI = request.getRequestURI();


        if(checkURI(requestURI)){
            filterChain.doFilter(request, response);
            return;
        }

        // 判断后台用户是否登录
        if(request.getSession().getAttribute("employee") != null) {
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("employee"));
            filterChain.doFilter(request, response);
            return;
        }

        // 判断移动端用户是否登录
        if(request.getSession().getAttribute("user") != null) {
            BaseContext.setCurrentId((Long) request.getSession().getAttribute("user"));
            filterChain.doFilter(request, response);
            return;
        }



        response.getWriter().write(JSON.toJSONString(R.error(0, "NOTLOGIN")));
    }

    public boolean checkURI(String uri){
        for(String u : URLS){
            if(PATH_MATCHER.match(u, uri)){
                return true;
            }
        }
        return false;
    }
}
