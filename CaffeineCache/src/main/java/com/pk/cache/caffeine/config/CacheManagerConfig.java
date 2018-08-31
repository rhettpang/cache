package com.pk.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Created by pangkunkun on 2018/8/17.
 */
@Configuration
public class CacheManagerConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheManagerConfig.class);

    @Value("${caffeine.spec}")
    private String caffeineSpec;

    @Autowired
    private CacheLoader cacheLoader;


    @Bean
    public CacheManager cacheManagerWithCacheLoading(){
        logger.info("cacheManagerWithCacheLoading" );
        Caffeine caffeine = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)
//                .refreshAfterWrite(5,TimeUnit.SECONDS)
                .expireAfterWrite(50,TimeUnit.SECONDS);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setAllowNullValues(true);
        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheLoader(cacheLoader);
        cacheManager.setCacheNames(getNames());
        return cacheManager;
    }



    @Bean(name = "caffeine")
    @Primary
    public CacheManager cacheManagerWithCaffeine(){
        logger.info("This is cacheManagerWithCaffeine");
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        Caffeine caffeine = Caffeine.newBuilder()
                //cache的初始容量值
                .initialCapacity(100)
                //maximumSize用来控制cache的最大缓存数量，maximumSize和maximumWeight不可以同时使用，
                .maximumSize(1000);
                //控制最大权重
//                .maximumWeight(100);
//                .expireAfter();
                //使用refreshAfterWrite必须要设置cacheLoader
//                .refreshAfterWrite(5,TimeUnit.SECONDS);
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheLoader(cacheLoader);
        cacheManager.setCacheNames(getNames());
//        cacheManager.setAllowNullValues(false);
        return cacheManager;
    }

    @Bean(name = "caffeineSpec")
    public CacheManager cacheManagerWithCaffeineFromSpec(){
        CaffeineSpec spec = CaffeineSpec.parse(caffeineSpec);
        Caffeine caffeine = Caffeine.from(spec);
        //此方法等同于上面from(spec)
//        Caffeine caffeine = Caffeine.from(caffeineSpec);

        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(caffeine);
        cacheManager.setCacheNames(getNames());
        return cacheManager;
    }

    /**
     * 可以实现自己生成key的策略
     * */
    @Bean(name = "SimpleKeyGenerator")
    public KeyGenerator keyGenerator(){
        return new SimpleKeyGenerator();
    }

    private static List<String> getNames(){
        List<String> names = new ArrayList<>(2);
        names.add("outLimit");
        names.add("notOutLimit");
        return names;
    }

}
