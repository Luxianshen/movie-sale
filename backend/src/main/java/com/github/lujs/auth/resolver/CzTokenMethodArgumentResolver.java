package com.github.lujs.auth.resolver;

import com.github.lujs.commmon.CzToken;
import com.github.lujs.commmon.annotation.Token;
import com.github.lujs.service.CzTokenService;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.annotation.Resource;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/8 19:59
 */
@Component
public class CzTokenMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final static String CZ_TOKEN_HEADER_KEY = "token";

    @Resource
    private CzTokenService tokenService;

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return methodParameter.getParameterType().isAssignableFrom(CzToken.class)
                && methodParameter.hasParameterAnnotation(Token.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        String token = nativeWebRequest.getHeader(CZ_TOKEN_HEADER_KEY);
        if (StringUtils.hasText(token)) {
            return tokenService.getCzToken(token);
        }
        throw new RuntimeException("系统报错了");
    }

}

