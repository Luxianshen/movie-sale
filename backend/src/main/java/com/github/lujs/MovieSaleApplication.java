package com.github.lujs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Lujs
 */
@SpringBootApplication(scanBasePackages = "com.github.**")
public class MovieSaleApplication {

    public static void main(String[] args) {
        SpringApplication.run(MovieSaleApplication.class, args);
    }
}
