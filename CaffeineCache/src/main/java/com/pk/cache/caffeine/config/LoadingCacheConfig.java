package com.pk.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadingCacheConfig {

    private static final Logger logger = LoggerFactory.getLogger(CacheManagerConfig.class);

    @Autowired
    private CacheLoader cacheLoader;

    /**
     * 用来加载数据
     * */
    @Bean(name = "LoadingCache")
    public LoadingCache cacheManagerWithAsyncCacheLoader(){
        logger.info("cacheManagerWithCacheLoading" );
        LoadingCache loadingCache = Caffeine.newBuilder()
                .recordStats()
                .build(cacheLoader);
        return loadingCache;
    }

}
