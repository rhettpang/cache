package com.pk.cache.caffeine.controller;

import com.pk.cache.caffeine.service.CaffeineService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@RestController
public class CaffeineController {
    private static final Logger logger = LoggerFactory.getLogger(CaffeineController.class);

    @Autowired
    private CaffeineService caffeineService;

    @GetMapping("/caffeine")
    public String CaffeineSaveTest(@RequestParam String name){
//        logger.info("CaffeineController name = " + name);
        return caffeineService.caffeineServiceTest(name);
    }

}
