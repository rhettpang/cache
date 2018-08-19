package com.pk.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;
/**
 * Created by pangkunkun on 2018/8/17.
 */
@Configuration
public class CaffeineConfig {

    private static final Logger logger = LoggerFactory.getLogger(CaffeineConfig.class);

    @Value("${caffeine.spec}")
    private String caffeineSpec;

    @Bean(name = "caffeine")
    @Primary
    public CacheManager cacheManagerWithCaffeine(){
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
        cacheManager.setCacheNames(getNames());
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

//    @Bean(name = "cacheLoader")
//    public CacheManager cacheManagerWithCacheLoading(){
//        Caffeine caffeine = Caffeine.newBuilder()
//                .initialCapacity(100)
//                .maximumSize(1000)
//                .refreshAfterWrite(60,TimeUnit.SECONDS);
//
//        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
//        cacheManager.setCacheLoader(cacheLoader());
//        cacheManager.setCaffeine(caffeine);
//        cacheManager.setCacheNames(getNames());
//        return cacheManager;
//    }
//

   /**
    * 只有设置了refreshAfterWrite这里才生效
    * 关于refreshAfterWrite和CacheLoader，只有当该key被调用之后才会执行这里
    * */
    @Bean
    public CacheLoader<Object,Object> cacheLoader(){
        CacheLoader<Object,Object> cacheLoader = new CacheLoader<Object, Object>() {
            @Override
            public Object load(Object key) throws Exception{
                System.out.println(System.currentTimeMillis()+" This is load key = " + key);
                if (String.valueOf(key).equals("kun")){
                    return "kun ni hao";
                }
                    return key + "ni hao";
            }

            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                System.out.println(System.currentTimeMillis()+" oldValue = " + oldValue);
                return this.load(key);
//                return oldValue+" a";
            }

            /**
             * 只要配置了这个方法，必定会先于reload执行
             * */
            @Override
            public CompletableFuture asyncReload(Object key, Object oldValue,  Executor executor) {
                logger.info("asyncReload key = {}, oldValue = {}",key,oldValue);

                requireNonNull(key);
                requireNonNull(executor);
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        logger.info("start to reload");
                        return reload(key, oldValue);
                    } catch (RuntimeException e) {
                        throw e;
                    } catch (Exception e) {
                        throw new CompletionException(e);
                    }
                }, executor);
            }

            @Override
            public CompletableFuture asyncLoad(Object key,  Executor executor) {
                System.out.println(System.currentTimeMillis()+" asyncLoadkey = " + key);
                return CompletableFuture.supplyAsync(() -> {
                    try {
                        return this.load(key);
                    } catch (RuntimeException var3) {
                        throw var3;
                    } catch (Exception var4) {
                        throw new CompletionException(var4);
                    }
                }, executor);
            }

        };
        return cacheLoader;
    }

    private static List<String> getNames(){
        List<String> names = new ArrayList<>(2);
        names.add("outLimit");
        names.add("notOutLimit");
        return names;
    }
}
