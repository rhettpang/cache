package com.pk.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.*;
import com.pk.cache.caffeine.service.CaffeineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheConfig.class);

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

    /**
     * writer的监控是同步执行的
     * */
    @Bean("writer")
    public Cache writerCache(){
        return Caffeine.newBuilder()
                .recordStats()
                .expireAfterWrite(5,TimeUnit.SECONDS)
                .writer(new CacheWriter<Object, Object>() {
                    @Override
                    public void write(Object key, Object value){
                        logger.info("This is writerCache's write");
                        //如果有缓存新增，这里的方法将被执行
                        //写操作是阻塞的，写的时候读数据会返回原有值
                    }
                    @Override
                    public void delete(Object key, Object value, RemovalCause cause){
                        logger.info("This is writerCache's delete");
                        //如果有缓存删除（到期等），这里的方法将被执行
                    }
                })
                .build(key -> caffeineService.getCacheService(String.valueOf(key)));
    }


    /**
     * RemovalListener的方法是异步执行的
     * */
    @Bean("RemovalListener")
    public Cache removalListenerCache(){
        return Caffeine.newBuilder()
                .recordStats()
                .refreshAfterWrite(5,TimeUnit.SECONDS)
//                .removalListener((key, value, cause) ->  myRemovalListener(key, value, cause))
                .build(cacheLoader);
//                .build(key -> caffeineService.getCacheService(String.valueOf(key)));
    }

    private void myRemovalListener(Object key, Object value, RemovalCause cause){
        logger.info("key = {}",key);
        logger.info("value = {}",value);
//        int i = 10/0;
        logger.info("This is myRemovalListener, removal key = {}, value = {}, cause = {}",key,value,cause);
    }

}
