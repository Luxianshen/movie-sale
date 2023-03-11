package com.github.lujs.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.github.lujs.commmon.CzToken;
import com.github.lujs.model.UserDto;
import com.github.lujs.model.WxLocation;
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
public class CzTokenService {

    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 创建Token
     * Token
     */
    public CzToken createCzToken(UserDto userDto, WxLocation location) {

        Assert.notNull(userDto);
        //前缀 和 权限
        String tokenKey = "CZ-" + IdUtil.getSnowflake(1L, 3L).nextIdStr();

        //base
        CzToken token = BeanUtil.toBean(userDto,CzToken.class);
        token.setToken(tokenKey);
        userDto.setLat(location.getLat());
        userDto.setLon(location.getLon());
        userDto.setCityName(location.getCityName());
        userDto.setCityCode(location.getCityCode());
        //缓存token 后端userId
        redisTemplate.opsForValue().set(tokenKey, userDto, 120, TimeUnit.MINUTES);
        return token;
    }

    public CzToken getCzToken(String key) {
        if (redisTemplate.hasKey(key)) {
            //序列化优化 后端获取数据 多一个userId
            CzToken czToken = BeanUtil.toBean(redisTemplate.opsForValue().get(key), CzToken.class);
            czToken.setToken(key);
            //免权限token 刷新缓存处
            //refresh(key);
            return czToken;
        }
        throw new RuntimeException("获取token失败");
    }


    public void destroy(String token) {
    }
}
