package com.pk.cache.caffeine.controller;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.pk.cache.caffeine.service.CaffeineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@RestController
public class CaffeineController {
    private static final Logger logger = LoggerFactory.getLogger(CaffeineController.class);

    @Autowired
    private CaffeineService caffeineService;

    @GetMapping("/stats")
    public CacheStats stats(){
        return caffeineService.stats();
    }

    @GetMapping("/cache/save")
    public void saveIntoCache(@RequestParam String key){
        caffeineService.saveCache(key);
    }

    @GetMapping("/cache/get")
    public Object getIntoCache(@RequestParam String key){
        return caffeineService.getCache(key);
    }

    @PostMapping("/caffeine")
    public String CaffeineSaveTest(@RequestParam String name){
        logger.info("CaffeineController name = " + name);
        return caffeineService.addCaffeineServiceTest(name);
    }

    @GetMapping("/caffeine")
    public String CaffeineGetTest(@RequestParam String name,@RequestParam Integer age){
        logger.info("CaffeineController name = " + name);
        name = caffeineService.getCaffeineServiceTest(name, age);
        System.out.println("end name = " + name);
        return name;
    }

    @PutMapping("/caffeine")
    public String CaffeineUpdateTest(@RequestParam String name){
        logger.info("CaffeineController name = " + name);
        return caffeineService.updateCaffeineServiceTest(name);
    }

    @DeleteMapping("/caffeine")
    public String CaffeineDeleteTest(@RequestParam String name){
        logger.info("CaffeineController name = " + name);
        return caffeineService.deleteCaffeineServiceTest(name);
    }

    @GetMapping("manager")
    public void CacheManagerTest(){
        caffeineService.testCacheManager();
    }

}
