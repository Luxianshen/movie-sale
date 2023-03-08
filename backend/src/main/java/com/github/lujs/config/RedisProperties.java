package com.github.lujs.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author Lujs
 * @desc TODO
 * @date 2023/3/8 20:06
 */
@Component
@ConfigurationProperties(prefix = "spring.redis")
@Data
public class RedisProperties {

    /**
     * 服务地址
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * 密码
     */
    private String password;

    /**
     * 数据库
     */
    private Integer database;

    /**
     * 前缀
     */
    private String keyPrefix;
}


