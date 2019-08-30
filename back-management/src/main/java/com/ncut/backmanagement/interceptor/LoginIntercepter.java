package com.ncut.backmanagement.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <b>System：</b>ncc<br/>
 * <b>Title：</b>LoginIntercepter<br/>
 * <b>Description：</b><br/>
 * <b>@author： </b>xiadong<br/>
 * <b>@date：</b>2019/8/7 17:14<br/>
 */
@Component
public class LoginIntercepter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Object userName = request.getSession().getAttribute("loginName");
/*        if (null == userName) {
            response.sendRedirect("/admin/login");
            return false;
        }*/
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
