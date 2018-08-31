package com.pk.cache.caffeine;

import com.spring4all.swagger.EnableSwagger2Doc;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Created by pangkunkun on 2018/8/17.
 */
@SpringBootApplication
@EnableCaching
@EnableSwagger2Doc
public class CaffeineApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(CaffeineApplication.class,args);
    }

    @Value("${dynamic.default-db}")
    private String db1;

//    @Value("${dynamic.defaultDb}")
//    private String db2;

    @Override
    public void run(String... args) throws Exception{
        System.out.println("db1 = " + db1);
//        System.out.println("db2 = " + db2);

    }
}
