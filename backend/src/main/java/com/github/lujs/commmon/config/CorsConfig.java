package com.github.lujs.commmon.config;

import com.github.lujs.commmon.config.properties.CorsProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.annotation.Resource;

/**
 * CORS 跨域配置
 * @author Lujs
 */
@Configuration
@ConditionalOnProperty(name = "cors.enable", havingValue = "true")
public class CorsConfig {

    @Resource
    private CorsProperties corsProperties;

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        if (StringUtils.hasText(corsProperties.getDomain())) {
            String[] domains = corsProperties.getDomain().split(";");
            for (String domain : domains) {
                config.addAllowedOrigin(domain);
            }
        }
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration(corsProperties.getPath(), config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

}
