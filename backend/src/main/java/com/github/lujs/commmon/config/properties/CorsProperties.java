package com.github.lujs.commmon.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: Lujs
 * @date: 2019/2/3
 * @desc:
 */
@Component
@ConfigurationProperties(prefix = "cors")
@Data
public class CorsProperties {

    private String domain;

    private String path;
}
