package com.github.lujs.config;

import cn.hutool.core.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/8 20:07
 */
@Component
@Slf4j
public class DefaultRedisKeySerializer implements RedisSerializer<String> {

    @Resource
    private RedisProperties redisProperties;

    private final Charset charset;

    public DefaultRedisKeySerializer() {
        this(StandardCharsets.UTF_8);
    }

    public DefaultRedisKeySerializer(Charset charset) {
        Assert.notNull(charset, "Charset must not be null!");
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        String keyPrefix = redisProperties.getKeyPrefix();
        String saveKey = new String(bytes, charset);
        int indexOf = saveKey.indexOf(keyPrefix);
        if (indexOf > 0) {
            log.warn("key缺少前缀");
        } else {
            saveKey = saveKey.substring(indexOf);
        }
        return (saveKey.getBytes() == null ? null : saveKey);
    }

    @Override
    public byte[] serialize(String string) {
        String keyPrefix = redisProperties.getKeyPrefix();
        String key =String.format("%s:%s", keyPrefix, string);
        return (key == null ? null : key.getBytes(charset));
    }
}

