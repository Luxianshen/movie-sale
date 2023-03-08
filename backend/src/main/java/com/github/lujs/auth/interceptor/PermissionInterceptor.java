package com.github.lujs.auth.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/8 19:57
 */
@Component
public class PermissionInterceptor implements HandlerInterceptor {
    private final static String PERMISSION_KEY = "org.springframework.web.servlet.HandlerMapping.bestMatchingPattern";

    private final static String BZ_TOKEN_HEADER_KEY = "BZ-Token";

    private static final List<String> WHITE_PATH_LIST = new ArrayList<>();



    public PermissionInterceptor() {
        WHITE_PATH_LIST.add("/error");
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        // 白名单直接通过验证
        if (WHITE_PATH_LIST.contains(request.getRequestURI())) return true;
        //只拦截mvc方法
        if (handler instanceof HandlerMethod) {
            //权限验证合法
                return true;
            //无权限访问
            //throw new PermissionException(AuthErrorEnums.NO_AUTH_ACCESS);
        }
        return true;
    }

}
