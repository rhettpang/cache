package com.pk.cache.caffeine.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@Service
public class CaffeineService {


    @Cacheable(cacheNames = "outLimit",key = "#name",sync = true)
    public String caffeineServiceTest(String name){
        String value = name + " nihao";
        System.out.println("value = "+value);
        return value;
    }

}
