package com.pk.cache.caffeine.controller;

import com.pk.cache.caffeine.service.CaffeineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@RestController
public class CaffeineController {

    @Autowired
    private CaffeineService caffeineService;

    @GetMapping("/caffeine")
    public String CaffeineSaveTest(@RequestParam String name){
        System.out.println(System.currentTimeMillis()+" name = " + name);
        return caffeineService.caffeineServiceTest(name);
    }

}
