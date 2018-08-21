package com.pk.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.*;
import com.pk.cache.caffeine.service.CaffeineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    @Autowired
    private CacheLoader cacheLoader;

    @Autowired
    private CaffeineService caffeineService;

    /**
     * maximumWeight基于Window TinyLfu策略（类似LRU:最近最少使用原则）丢弃缓存
     * */
    @Bean("Weight")
    public Cache weightCache(){
        return Caffeine.newBuilder()
                .recordStats()
                //maximumWeight与weigher结合使用，当weigher中的权重值（weigher权重的初始值 * 缓存个数）超过maximumWeight的值时，新增记录的时候删除weigher值最小的缓存
                .maximumWeight(3)
                //这里记录了一个键值对的权重
                .weigher((k,v)->1)
                .build();
    }


    /**
     * maximumSize基于Window TinyLfu策略（类似LRU:最近最少使用原则）丢弃缓存
     * */
    @Bean
    @Primary
    public Cache sizeCache(){
        return Caffeine.newBuilder()
                .recordStats()
                .maximumSize(5)
                .build();
    }

    /**
     * Expiry自定义按时间失效方法
     * */
    @Bean("Expiry")
    public Cache expiryCache(){
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfter(new Expiry<Object, Object>() {
                    @Override
                    public long expireAfterCreate(Object key, Object value, long currentTime){
                        return currentTime;
                    }

                    @Override
                    public long expireAfterUpdate(Object key, Object value, long currentTime,  long currentDuration){
                        return currentDuration;
                    }

                    @Override
                    public long expireAfterRead(Object key, Object value, long currentTime,  long currentDuration){
                        return currentDuration;
                    }
                })
                .build();
    }

    /**
     * 根据写入的时间计算过期时间
     * */
    @Bean("expireAfterWrite")
    public Cache expireAfterWriteCache(){
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(10,TimeUnit.SECONDS)
                .build();
    }

    /**
     * 根据访问的时间计算过期时间
     * */
    @Bean("expireAfterAccess")
    public Cache expireAfterAccessCache(){
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterAccess(10,TimeUnit.SECONDS)
                .build();
    }

    @Bean("refreshAfterWrite")
    public Cache refreshAfterWriteCache(){
        return Caffeine.newBuilder()
                .recordStats()
                .refreshAfterWrite(10,TimeUnit.SECONDS)
                .build(key -> caffeineService.getCacheService(String.valueOf(key)));
    }

}
