package com.pk.cache.caffeine.config;

import com.github.benmanes.caffeine.cache.CacheLoader;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@Configuration
public class CaffeineConfig {

//
//
//
//    @Bean
//    public CacheManager cacheManager(){
//        CacheManager cacheManager = new CaffeineCacheManager();
//
//
//        return cacheManager;
//    }
//


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

            /**
             *
             * */
            @Override
            public Object reload(Object key, Object oldValue) throws Exception {
                System.out.println(System.currentTimeMillis()+" oldValue = " + oldValue);
                CompletableFuture future = asyncLoad(key, asyncTaskExecutor());
                Object newValue = "";
                while (true){
                    if (future.isDone()){
                        newValue = future.get();
                        break;
                    }
                }
                System.out.println("newValue = "+newValue);
                return newValue;
//                return oldValue+" a";
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


    @Bean
    public AsyncTaskExecutor asyncTaskExecutor(){
        AsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
//        asyncTaskExecutor.
        return asyncTaskExecutor;
    }

}
