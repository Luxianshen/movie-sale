package com.github.lujs.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.github.lujs.commmon.CzToken;
import com.github.lujs.model.UserDto;
import com.github.lujs.model.pojo.WxLocation;
import com.github.lujs.service.ITokenService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/8 20:14
 */
@Service
public class CzTokenService implements ITokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建Token
     * Token
     */
    @Override
    public CzToken createToken(UserDto userDto, WxLocation location) {

        Assert.notNull(userDto);
        //前缀 和 权限
        String tokenKey = "CZ-" + IdUtil.getSnowflake(1L, 3L).nextIdStr();

        //base

        userDto.setLat(location.getLat());
        userDto.setLon(location.getLon());
        userDto.setCityId(location.getCityId());
        userDto.setCityName(location.getCityName());
        userDto.setCityCode(location.getCityCode());

        CzToken token = BeanUtil.toBean(userDto, CzToken.class);
        token.setToken(tokenKey);
        //缓存token 后端userId
        redisTemplate.opsForValue().set(tokenKey, userDto, 120, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public CzToken getToken(String key) {
        if (redisTemplate.hasKey(key)) {
            //序列化优化 后端获取数据 多一个userId
            CzToken czToken = BeanUtil.toBean(redisTemplate.opsForValue().get(key), CzToken.class);
            czToken.setToken(key);
            //免权限token 刷新缓存处
            refresh(key);
            return czToken;
        }
        throw new RuntimeException("获取token失败");
    }


    @Override
    public void refresh(String key) {
        redisTemplate.expire(key, 120, TimeUnit.MINUTES);
    }

    @Override
    public void destroy(String token) {
    }
}
