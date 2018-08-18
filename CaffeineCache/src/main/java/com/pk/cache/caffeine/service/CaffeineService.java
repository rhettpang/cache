package com.pk.cache.caffeine.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@Service
public class CaffeineService {
    private static final Logger logger = LoggerFactory.getLogger(CaffeineService.class);

    @Cacheable(cacheNames = "outLimit",key = "#name")
    public String caffeineServiceTest(String name){
        String value = name + " nihao";
        logger.info("CaffeineService value = {}",value);
        return value;
    }

}
