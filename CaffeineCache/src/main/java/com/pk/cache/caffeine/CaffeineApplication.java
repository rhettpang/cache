package com.pk.cache.caffeine;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@SpringBootApplication
@EnableCaching
@EnableSwagger2Doc
public class CaffeineApplication {
    public static void main(String[] args) {
        SpringApplication.run(CaffeineApplication.class,args);
    }
}
